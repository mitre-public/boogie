#!/bin/sh

set -e

fancy_echo() {
  local fmt="$1"; shift
  printf " >> $fmt\\n" "$@"
}

url_encode ()
{
    local URL="$1"
    echo "$URL" | sed -e 's/ /%20/g'
}

fetch_url ()
{
    local URL="$1"
    local TARGET="$2"
    # https://stackoverflow.com/a/677212/677201
    if command -v curl > /dev/null 2>&1; then
        >&2 echo "Fetching '${URL}' using curl"
        curl -sSk "${URL}" -o "${TARGET}"
    elif command -v wget > /dev/null 2>&1; then
        >&2 echo "Fetching '${URL}' using wget"
        wget -q --no-check-certificate "${URL}" -O "${TARGET}"
    else
        >&2 echo "No wget or curl found to fetch ${URL}!"
        exit 1
    fi

    if [ ! -f "${TARGET}" ]; then
        >&2 echo "${TARGET} is missing after download attempt, couldn't download ${URL}"
        exit 1
    fi
}

add_certificate_if_alias_not_present() {
  local keystore="$1"
  local alias=$2
  local cert="$3"
  # Check that cert is not there already
  local ALL_CERTS=$( "${JAVA_HOME}/bin/keytool" -list -keystore "$keystore" -storepass changeit )
  if [[ $ALL_CERTS == *"$alias"* ]]; then
    fancy_echo "${alias} is already there. Skipping."
  else
    "${JAVA_HOME}/bin/keytool" -import -alias "${alias}" -file "${cert}" -keystore "${keystore}" -storepass changeit -noprompt
  fi
}

install_java_certs() {
  local cert_dir="/tmp/ca-certificates"
  if [ ! -d "$cert_dir" ]; then mkdir -p $cert_dir; fi

  fancy_echo "Downloading MITRE SSL certificates into $cert_dir..."
  fetch_url http://pki.mitre.org/MITRE%20BA%20ROOT.crt "$cert_dir/MITRE-BA-ROOT.crt"
  fetch_url http://pki.mitre.org/MITRE%20BA%20NPE%20CA-1.crt "$cert_dir/MITRE-BA-NPE-CA-1.crt"
  fetch_url http://pki.mitre.org/MITRE%20BA%20NPE%20CA-3.crt "$cert_dir/MITRE-BA-NPE-CA-3.crt"
  fetch_url http://pki.mitre.org/MITRE%20BA%20NPE%20CA-4.crt "$cert_dir/MITRE-BA-NPE-CA-4.crt"

  fancy_echo "Exporting ZScaler root certificate into $cert_dir..."
  fetch_url http://pki.mitre.org/ZScaler_Root.crt "$cert_dir/zscaler_root_ca.crt"

  echo "Current Certs:"
  echo "$(ls "${cert_dir}")"

  local keystore8="$JAVA_HOME/jre/lib/security/cacerts"
  local keystore11="$JAVA_HOME/lib/security/cacerts" # after java 8, no more jre dir
  local keystores=( "$keystore8" "$keystore11" )

  for ks in "${keystores[@]}"; do
    if [ -e "${ks}" ]; then
      fancy_echo "Installing SSL certificates into ${ks}"
      add_certificate_if_alias_not_present "${ks}" mitre_ba_root "${cert_dir}/MITRE-BA-ROOT.crt"
      add_certificate_if_alias_not_present "${ks}" mitre_ba_npe_ca1 "${cert_dir}/MITRE-BA-NPE-CA-1.crt"
      add_certificate_if_alias_not_present "${ks}" mitre_ba_npe_ca3 "${cert_dir}/MITRE-BA-NPE-CA-3.crt"
      add_certificate_if_alias_not_present "${ks}" mitre_ba_npe_ca4 "${cert_dir}/MITRE-BA-NPE-CA-4.crt"
      add_certificate_if_alias_not_present "${ks}" zscaler_root_ca "${cert_dir}/zscaler_root_ca.crt"
    fi
  done
}

# runit
install_java_certs