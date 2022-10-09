#!/bin/bash

if [[ "${BASE_URL}" == "" ]]; then
  echo "BASE_URL not set! Use, e.g." 1>&2
  echo "export BASE_URL=\"http://localhost:9082\"" 1>&2
  exit 1
fi

if [[ "$(which jq)" == "" ]]; then
  echo
  echo "You must have jq in the PATH!"
  exit 1
fi

if (( $# < 1 )); then
  echo "Usage: $0 <taskType> <taskInputParameter> <clientRequestId>" 1>&2
  echo "  e.g.: $0 Fibonacci 5 \$RANDOM" 1>&2
  exit 1
fi

taskType="${1}"
taskInputParameter="${2}"
clientRequestId="${3}"

url="${BASE_URL}/api/v1/tasks"
echo "POST to ${url}" 1>&2

result=$(curl -X POST --silent \
    --header 'Accept: application/json' --header 'Content-Type: application/json' \
    --data "{ \"type\": \"${taskType}\", \"parameter\": { \"input\": ${taskInputParameter} }, \"client_request_id\": \"${clientRequestId}\" }" \
    --write-out "%{errormsg}" \
    "${url}")

CURL_STATUS=$?

if [[ $CURL_STATUS -eq 7 ]]; then
  echo "Error: curl status = Cannot connect to $BASE_URL" 1>&2
  exit 1
elif [[ $CURL_STATUS -ne 0 ]]; then
  echo "Error: curl status = $CURL_STATUS" 1>&2
  exit 1
fi

taskReferenceId=$(echo "${result}" | jq -r ".task_reference_id")

if [[ "$taskReferenceId" != "" ]]; then
  echo "taskReferenceId=${taskReferenceId}"
else
  echo ">>> ${result}" 1>&2
fi
