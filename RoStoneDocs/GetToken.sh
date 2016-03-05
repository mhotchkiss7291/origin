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
--compressed \
| python -mjson.tool

