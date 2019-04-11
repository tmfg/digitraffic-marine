#!/usr/bin/env bash

OUT_DIR=${1:?Dir for saved files is required parameter.}

BASEDIR=$(cd "$(dirname "$0")"; pwd)

cd ${BASEDIR}

echo "Running at dir $(pwd)"

SCHEMA=$(curl -s https://meri-test.digitraffic.fi/api/v1/metadata/documentation/v2/api-docs?group=metadata-api | jq)

SERVICE_HOST=$(echo ${SCHEMA} | jq -r '.host' )
echo SERVICE_HOST: $SERVICE_HOST

SERVICE_PATHS=$(echo ${SCHEMA} | jq -r '.paths | keys | .[]')

mkdir -p ${OUT_DIR}

LEN=0

for SERVICE_PATH in $SERVICE_PATHS; do
    if [ ${#SERVICE_PATH} -gt ${LEN} ] && [[ ! ${SERVICE_PATH} =~ .*{.* ]]; then
        LEN=${#SERVICE_PATH}
    fi
done

for SERVICE_PATH in $SERVICE_PATHS; do
    if [[ ! ${SERVICE_PATH} =~ .*{.* ]]; then
        OUT=${OUT_DIR}/${SERVICE_PATH//\//_}.json
        SERVICE_PATH_PADDED=$(printf %-${LEN}s $SERVICE_PATH)
        echo "Saving ${SERVICE_HOST}${SERVICE_PATH_PADDED} to ${OUT}"
        $(curl -s ${SERVICE_HOST}${SERVICE_PATH} | jq > ${OUT})
    fi
done
