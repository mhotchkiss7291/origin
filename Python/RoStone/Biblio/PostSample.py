from urllib import urlencode

import requests
import json
import PythonTokenGenerator

# Why does this not work mirroring the working cURL command with a valid auth_token?!!

# The working cURL command that I am trying to make work:
# curl \
# 'https://biblio-qa1.dev.rosettastone.com/content/activity_set/summary/' -X POST \
# -H 'Accept: application/json' \
# -H 'Accept-Language: en-US' \
# -H 'Accept-Encoding: gzip, deflate' \
# -H 'DNT: 1' \
# -H 'X-Livemocha-Product: Product:Aria' \
# -H 'Content-Type: application/json' \
# -H 'Authorization: Bearer 9288af7f-a460-460e-b75c-b51bc9a658ef' \
# --data '{activitySetIds:["4c9de789-5663-4c7f-9553-9f90fb156b83"]}' \
# | python -mjson.tool (which makes the JSON look pretty)


# Strings
url = "https://biblio-qa1.dev.rosettastone.com"
endpoint = "/content/activity_set/summary/"

# For auth_token
user = "mhotchkiss-qa1@rosettastone.com"
environment = "qa1"
response = PythonTokenGenerator.get_token(environment, user)
user_id = response[0]
auth_token = response[1]
print('auth_token = ' + auth_token + '\n')

header_data = {
    'Accept': 'application/json',
    'Accept-Language': 'en-US',
    'Accept-Encoding': 'gzip, deflate',
    'DNT': '1',
    'X-Livemocha-Product': 'Product:Aria',
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + auth_token
}

json_data = {
    'activitySetIds': '4c9de789-5663-4c7f-9553-9f90fb156b83'
}

# Tried this but no help
#payload = urlencode(json_data)
#response = requests.post(url + endpoint, data=payload, headers=header_data)

response = requests.post(url + endpoint, data=json_data, headers=header_data)

# Process and format the response
results = json.loads(response.text)
pretty = json.dumps(results, indent=2)
print(pretty)

#Results

# auth_token = 9288af7f-a460-460e-b75c-b51bc9a658ef

#
# {
#   "error_description": "You have issued an invalid request that could not be processed by the web service",
#   "error": "internal_service_error"
# }

# Process finished with exit code 0