#!/usr/bin/bash

URL="http://localhost:8080/festival"

mkdir -p ./responses  # Ensure the output directory exists
TOKEN=$1
file=./festival.json
filename=$(basename "$file" .json)

rm -rf ./responses/*.json

echo "Posting $file"

curl -X POST "$URL" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN"\
     -d @"$file" \
     -o "./responses/response_$filename.json" -v
