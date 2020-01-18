#!/bin/bash

cd $(dirname $0) || exit

./clear.sh
roo script --file gen.roo
