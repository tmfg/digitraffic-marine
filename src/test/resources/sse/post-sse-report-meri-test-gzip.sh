#!/usr/bin/env bash
gzip -c sse_export_test_6.json > sse_export_test_6.json.gz
curl -i --header "Content-Type: application/json" --header "Content-Encoding: gzip" --request POST --data-binary @sse_export_test_6.json.gz http://meri-test.digitraffic.fi/api/v1/sse/add