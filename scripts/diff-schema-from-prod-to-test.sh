#!/usr/bin/env bash


curl -s https://meri.digitraffic.fi/v2/api-docs?group=marine-api | jq > prod.json && curl -s https://meri-test.digitraffic.fi/api/v1/metadata/documentation/v2/api-docs?group=marine-api |jq > test.json && diffmerge prod.json test.json
