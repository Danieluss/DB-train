#!/bin/bash

cd "$(dirname "$0")" || exit
source environment.sh
docker stop ${POSTGRES_NAME}