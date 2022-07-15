# Dockerfile for image which will perform the file sync process
FROM ubuntu:18.04 AS base

RUN apt-get update && apt-get upgrade \
&& apt-get install -y --no-install-recommends curl \
&& apt-get install -y --no-install-recommends unzip \
&& apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /script
COPY ./cifp-download.sh .

WORKDIR /data/arinc
CMD ["bash", "/script/cifp-download.sh"]