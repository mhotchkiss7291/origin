import requests
import json
from PythonTokenGenerator import get_token

# cURL command to be converted
# curl -X GET -H "Accept: application/json" -H "Authorization: Bearer $auth_token" $bib_host/content/user_activity_set_summary/$user_id

# Test environment URL
url = "https://biblio-qa2.dev.rosettastone.com"

# Set service end point, user, environment
end_point = "/schedule/join/attendee?meetingID=testmeeting&host=rosettastoneqadev.adobeconnect.com"
#end_point = "/content/user_activity_set_summary/"
user = "mhotchkiss@rosettastone.com"
environment = "qa2"

# Get the generated userId and auth_token
response = get_token(environment, user)
user_id = response[0]
auth_token = response[1]

headers = {
    'Authorization: Bearer ' + auth_token,
    'X-Rosettastone-Product-Id: product.82692f39-a40e-4051-a6dc-e0cbf027c80b',
    'X-Livemocha-Product: Product:Aria'
}

# This isn't working yet
rest_response = requests.get(url + end_point + user_id, headers=headers)
results = json.loads(rest_response.text)
print(json.dumps(results))


