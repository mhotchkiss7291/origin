curl \
'https://prism-qa2.dev.rosettastone.com/reports/usage/product/Product%3AAria/user/a7bc1589-ee54-4a35-a2ce-ac2e5c31d712' \
-H 'Host: prism-qa2.dev.rosettastone.com' \
-H 'User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0' \
-H 'Accept: application/json, text/plain, */*' \
-H 'Accept-Language: en-US,en;q=0.5' \
-H 'Accept-Encoding: gzip, deflate, br' \
-H 'Authorization: Bearer 88663309-7cb4-422d-8d3c-2195f7090780' \
-H 'X-Requested-With: Ceres' \
-H 'Referer: https://advancedenglish-qa2.dev.rosettastone.com/' \
-H 'Origin: https://advancedenglish-qa2.dev.rosettastone.com' | python -mjson.tool