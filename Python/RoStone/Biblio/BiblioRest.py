import requests
import json

# cURL command to be converted
#curl -X GET -H "Accept: application/json" -H "Authorization: Bearer $auth_token" $bib_host/content/user_activity_set_summary/$user_id

# Test environment URL
url="https://biblio-qa2.dev.rosettastone.com"

# Service end point
end_point="/content/user_activity_set_summary/"

# User credentials
# Need a program to generate this from the
# login like idenifier: mhotchkiss@rosettastone.com
user_id="8c520215-1e36-4bc4-a33b-009ddcc7248a"

# This expires every few hours and needs to be renewed
# Need to add the http://cloudan-toolkit.trstone.com/tokenizer/#
# utility so that expired tokens will be renewed
auth_token="904b715a-c77e-48df-89f9-1131fe345fc7"


headers = {
    'Accept': 'application/json',
    'Authorization': 'Bearer ' + auth_token
}

# This script converts cURL commands to python by constructing the command by variables:
# url
# endpoint
# auth_token

r = requests.get(url + end_point + user_id , headers=headers)
results = json.loads(r.text)
print(json.dumps(results))




