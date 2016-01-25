import requests
import json

# cURL command to be converted
#curl -X GET -H "Accept: application/json" -H "Authorization: Bearer $auth_token" $bib_host/content/user_activity_set_summary/$user_id

url="https://biblio-qa2.dev.rosettastone.com"
user_id="8c520215-1e36-4bc4-a33b-009ddcc7248a"
auth_token="b9a45f08-99cc-46ad-949e-586a8251d2a6"
end_point="/content/user_activity_set_summary/"


headers = {
    'Accept': 'application/json',
    'Authorization': 'Bearer ' + auth_token
}

r = requests.get(url + end_point + user_id , headers=headers)
results = json.loads(r.text)
print(json.dumps(results))




