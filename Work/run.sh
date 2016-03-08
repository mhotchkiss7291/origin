curl \
'https://biblio-qa2.dev.rosettastone.com/content/activity_set/9a90f7b2-8770-4608-a925-7063236fdce3' \
-H 'Authorization: Bearer 4f1aaf05-57c5-4616-a2d2-99c4715f7095' \
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
