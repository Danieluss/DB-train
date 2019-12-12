#!/bin/bash

cd "$(dirname "$0")" || exit
source environment.sh
mkdir -p ${POSTGRES_VOLUME}
docker run --rm --name ${POSTGRES_NAME} -e POSTGRES_PASSWORD=train -d -p ${POSTGRES_PORT}:5432 -v ${POSTGRES_VOLUME}:/var/postgres/data ${POSTGRES_IMG}