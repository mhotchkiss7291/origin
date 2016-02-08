rm *.out

curl \
'https://biblio-qa1.dev.rosettastone.com/content/productCourseSummary/Product:Aria/en-US' \
-H 'X-Livemocha-Product: Product:Aria' \
-H 'Authorization: Bearer 00eea228-c676-4162-8627-b63ceadb2bae' \
| python -mjson.tool > 5.out

# Edit the activity set by adding ""version": 2," to CouchDB/Futon
# Wait for the cache to be cleared

# Query again but includeVersion=true 
curl \
'https://biblio-qa1.dev.rosettastone.com/content/productCourseSummary/Product:Aria/en-US?includeVersion=true' \
-H 'X-Livemocha-Product: Product:Aria' \
-H 'Authorization: Bearer 00eea228-c676-4162-8627-b63ceadb2bae' \
| python -mjson.tool > 6.out

echo "diffing..."
diff 5.out 6.out 

#6634c6681,6682
#<                     "validationErrors": []
#---
#>                     "validationErrors": [],
#>                     "version": 2
#6749c6797,6798
#<                     "validationErrors": []
#---
#>                     "validationErrors": [],
#>                     "version": 2
#7228c7277
#<     "hashCode": -855685028,
#---
#>     "hashCode": 1746307316,
#
