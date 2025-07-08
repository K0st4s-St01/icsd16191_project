#!/usr/bin/bash

URL="http://localhost:8080/performance/1/staff/karen"

mkdir -p ./responses  # Ensure the output directory exists
TOKEN=$1

rm -rf ./responses/*.json

echo "Posting $file"

curl -X POST "$URL" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN"\
     -o "./responses/response.json" -v
