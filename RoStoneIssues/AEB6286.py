import requests
import json
import PythonTokenGenerator

# Test environment URL
url = "https://biblio-qa1.dev.rosettastone.com"
activity_set = "ff3d2c6b-77a2-4a42-b9ce-8bae3aead76e"

# Set service end point, user, environment
end_point = "/content/activity_set/" + activity_set + "/aggregate"
user = "mhotchkiss-qa1@rosettastone.com"
environment = "qa1"

# Get the generated userId and auth_token
response = PythonTokenGenerator.get_token(environment, user)
user_id = response[0]
auth_token = response[1]

headers = {
    'Authorization': 'Bearer ' + auth_token
}

# Make the request
r = requests.get(url + end_point + user_id, headers=headers)

# Process and format the response
results = json.loads(r.text)
pretty = json.dumps(results, indent=2)
print(pretty)


