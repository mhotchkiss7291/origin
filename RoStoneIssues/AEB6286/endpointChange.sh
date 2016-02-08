rm *.out

curl \
'https://biblio-qa1.dev.rosettastone.com/content/productCourseSummary/Product:Aria/en-US' \
-H 'X-Livemocha-Product: Product:Aria' \
-H 'Authorization: Bearer 00eea228-c676-4162-8627-b63ceadb2bae' \
| python -mjson.tool > x

curl \
'https://biblio-qa1.dev.rosettastone.com/content/productCourseSummary/Product:Aria/en-US?includeVersion=true' \
-H 'X-Livemocha-Product: Product:Aria' \
-H 'Authorization: Bearer 00eea228-c676-4162-8627-b63ceadb2bae' \
| python -mjson.tool > y
