#!/usr/bin/bash

URL="http://localhost:8080/user"

mkdir -p ./responses  # Ensure the output directory exists

for file in ./dir_create_users/*.json;do

  filename=$(basename "$file" .json)

  echo "Posting $file"

  curl -X POST "$URL" \
       -H "Content-Type: application/json" \
       -d @"$file" \
       -o "./responses/response_$filename.json"
done
