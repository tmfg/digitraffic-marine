#!/usr/bin/env bash
curl -i --header "Content-Type: application/json" --request POST --data @example-sse-report.json http://localhost:9001/api/v1/sse/add
