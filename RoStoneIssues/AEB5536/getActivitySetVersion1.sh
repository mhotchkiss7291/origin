curl \
'https://biblio-qa2.dev.rosettastone.com/content/activity_set/c5fe6484-c72b-467f-bcc0-76f19b9f03b6?version=2' \
-H 'Authorization: Bearer 4cc06aff-8120-4f34-b32a-eea7833f5538' \
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
