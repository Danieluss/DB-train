#!/bin/bash

cd "$(dirname "$0")" || exit
source environment.sh
sudo apt-get install wget ca-certificates
echo "deb http://apt.postgresql.org/pub/repos/apt/ 18.04-pgdg main" >> /etc/apt/sources.list
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo apt-get update
sudo apt install postgresql-client

docker pull postgres:12