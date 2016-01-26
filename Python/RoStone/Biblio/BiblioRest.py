import requests
import json
import PythonTokenGenerator

# cURL command to be converted
# curl -X GET -H "Accept: application/json" -H "Authorization: Bearer $auth_token" $bib_host/content/user_activity_set_summary/$user_id

# Test environment URL
url = "https://biblio-qa2.dev.rosettastone.com"

# Set service end point, user, environment
end_point = "/content/user_activity_set_summary/"
user = "mhotchkiss@rosettastone.com"
environment = "qa2"

# Get the generated userId and auth_token
response = PythonTokenGenerator.get_token(environment, user)
user_id = response[0]
auth_token = response[1]

headers = {
    'Accept': 'application/json',
    'Authorization': 'Bearer ' + auth_token
}

r = requests.get(url + end_point + user_id, headers=headers)
results = json.loads(r.text)
print(json.dumps(results))


