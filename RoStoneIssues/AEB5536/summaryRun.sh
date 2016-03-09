curl \
'https://biblio-qa2.dev.rosettastone.com/content/productCourseSummary/Product:Aria/en-US?includeVersion=true' \
-H 'X-Livemocha-Product: Product:Aria' \
-H 'Content-Type: application/json;charset=UTF-8' \
-H 'Authorization: Bearer 43f0ffeb-1592-4060-a2c2-cf2ad5183d33' \
-H 'Origin: https://aebeditor-qa2.dev.rosettastone.com' \
-H 'Accept-Encoding: gzip, deflate, sdch' \
-H 'Accept-Language: en-US,en;q=0.8' \
-H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36' \
-H 'Accept: application/json, text/plain, */*' \
-H 'Referer: https://aebeditor-qa2.dev.rosettastone.com/' \
-H 'X-Requested-With: Ceres' \
-H 'Connection: keep-alive' \
--compressed \
 | python -mjson.tool
