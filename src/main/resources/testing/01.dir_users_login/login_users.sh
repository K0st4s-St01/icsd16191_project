#!/usr/bin/bash

URL="http://localhost:8080/user/login"

mkdir -p ./responses  # Ensure the output directory exists

rm -rf ./responses/*.json

for file in ./*.json;do

  filename=$(basename "$file" .json)

  echo "Posting $file"

  curl -X POST "$URL" \
       -H "Content-Type: application/json" \
       -d @"$file" \
       -o "./responses/response_$filename.json" -v
done
