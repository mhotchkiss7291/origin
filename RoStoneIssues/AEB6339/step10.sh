curl \
'https://biblio-qa2.dev.rosettastone.com/content/lesson/ffdce9b9-64ed-4157-ab33-45178e74bbe1/new' \
-X POST \
-H 'Authorization: Bearer 97d54476-acb6-40cd-b94f-5b6d16472804' \
| python -mjson.tool
