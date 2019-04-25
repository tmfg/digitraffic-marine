#!/usr/bin/env bash
#curl -i --header "Content-Type: application/json" --request POST --data @example-sse-report1.json http://localhost:9001/api/v1/sse/add

curl -i --header "Content-Type: application/json" --request POST --data @sse_export_test_6.json http://localhost:9001/api/v1/sse/add