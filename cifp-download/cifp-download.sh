# This script scrapes the FAA web portal for the current cycle of CIFP data and downloads the ARINC-424
# theirin to a file `arinc-data.dat` in the working directory (cleaning up any transient resources)

set -e

CIFP_PATTERN="https:\/\/aeronav\.faa\.gov\/Upload_313-d\/cifp\/CIFP_.{6}\.zip"

# Generating airac cycles from dates is hard - just scrape the HTML and see whats available
echo "Inspecting CIFP portal for available files"
AVAILABLE_URLS=$(curl --insecure https://www.faa.gov/air_traffic/flight_info/aeronav/digital_products/cifp/download/ | grep -ioE $CIFP_PATTERN)
echo "Located files: $AVAILABLE_URLS in portal"

IFS=';' read -ra AVAILABLE_URLS_ARRAY <<< "$AVAILABLE_URLS"

# Grab the first of the listed cycles of data on the page (this should be the current cycle)
TARGET_URL=${AVAILABLE_URLS_ARRAY[0]}
echo "Extracting data from target CIFP file at: $TARGET_URL"

TARGET_FILENAME=$(echo $TARGET_URL | grep -ioE "CIFP_.{6}")
echo "Target filename: $TARGET_FILENAME"

# Curl the data at the extension
curl --insecure -O $TARGET_URL
# Unpack the ZIP archive and pipe it to a nice named file - pipe the text into a new tmp file and then replace the
# existing file with that one (if it exists via mv) as opposed to appending to the end of it...
unzip -p "$TARGET_FILENAME.zip" "FAACIFP18" > arinc-data-tmp.dat
mv arinc-data-tmp.dat arinc-data.dat

rm -rf "$TARGET_FILENAME.zip"