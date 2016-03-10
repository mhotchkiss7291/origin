#!/bin/bash

readonly PROGNAME=$(basename $0)
readonly ARGS="$@"

echo
if [ $# -ne 2 ]; then
        echo 1>&2 Usage: $0 environment user_email
        echo
        echo 1>&2 Example: $0 qa1 user@rosettastone.com
        exit 127
fi

ENVIRONMENT=$1
USER_EMAIL=$2

TULLY_URL="tully-"
TULLY_URL+=$ENVIRONMENT
TULLY_URL+=".dev.rosettastone.com"
echo $TULLY_URL
echo $USER_EMAIL

curl \
'http://cloudan-toolkit.trstone.com/tokenizer/authenticate/' \ 
-H 'Cookie: csrftoken=wP0cBv5mHNQnTuM5BfshbwEgk7ewzyNV' \ 
-H 'Origin: http://cloudan-toolkit.trstone.com' \
-H 'Accept-Encoding: gzip, deflate' \
-H 'Accept-Language: en-US,en;q=0.8' \
-H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36' \
-H 'Content-Type: application/json' \
-H 'Accept: */*' \
-H 'Referer: http://cloudan-toolkit.trstone.com/tokenizer/' \
-H 'X-CSRFToken: wP0cBv5mHNQnTuM5BfshbwEgk7ewzyNV' \
-H 'X-Requested-With: XMLHttpRequest' \
-H 'Connection: keep-alive' --data-binary \ 
'{"client":"client.aeb.web","instance":"tully-qa2.dev.rosettastone.com","grant_type":"password","user_id":"mhotchkiss-learner@rosettastone.com","password":"password"}' \
--compressed
