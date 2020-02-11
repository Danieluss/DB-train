#!/bin/bash

# unused
cd "$(dirname "$0")" || exit
source environment.sh
docker build . -t ${POSTGRES_IMG}