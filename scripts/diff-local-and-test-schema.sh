#!/usr/bin/env bash


curl -s http://localhost:9001/v2/api-docs?group=metadata-api | jq > local.json && curl -s https://meri-test.digitraffic.fi/api/v1/metadata/documentation/v2/api-docs?group=metadata-api |jq > test.json && diffmerge test.json local.json
