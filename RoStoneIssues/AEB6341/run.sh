curl \
https://prism-qa2.dev.rosettastone.com/reports/usage/product/Product%3AAria/user/8421e7ea-5f00-4178-bf4a-f6ff761a2ea3 \
-H 'Authorization: Bearer f1257bf9-a3a7-4623-b2f1-7213d60ba7ce' \
| python -mjson.tool
