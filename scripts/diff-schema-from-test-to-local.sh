#!/usr/bin/env bash


curl -s https://meri-test.digitraffic.fi/api/v1/metadata/documentation/v2/api-docs?group=marine-api | jq > test.json && curl -s http://localhost:9001/v2/api-docs?group=marine-api |jq > local.json && diffmerge test.json local.json
