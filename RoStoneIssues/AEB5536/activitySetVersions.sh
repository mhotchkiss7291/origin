curl \
'https://biblio-qa2.dev.rosettastone.com/content/activity_set/d45b2062771e16f780ec0aa740aa4e05' \
-H 'Authorization: Bearer f16898f4-c3fc-47d0-adc5-0f2e71e4c3bf' \
-H 'Origin: https://aebeditor-qa2.dev.rosettastone.com' \
-H 'Accept-Encoding: gzip, deflate, sdch' \
-H 'Accept-Language: en-US,en;q=0.8' \
-H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36' \
-H 'Accept: application/json, text/plain, */*' \
-H 'Referer: https://aebeditor-qa2.dev.rosettastone.com/' \
-H 'X-Requested-With: Ceres' \
-H 'X-Livemocha-Product: Product:Aria' \
-H 'Connection: keep-alive' \
--compressed \
 | python -mjson.tool
