curl \
'https://biblio-qa2.dev.rosettastone.com/content/lesson/no-such-activity-set/new' \
-X POST \
-H 'Authorization: Bearer 97d54476-acb6-40cd-b94f-5b6d16472804' \
| python -mjson.tool
