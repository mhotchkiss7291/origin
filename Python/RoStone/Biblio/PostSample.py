from urllib import urlencode

import requests
import json
import PythonTokenGenerator

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

url = "https://biblio-qa1.dev.rosettastone.com"
endpoint = "/content/activity_set/summary/"
user = "mhotchkiss-qa1@rosettastone.com"
environment = "qa1"

response = PythonTokenGenerator.get_token(environment, user)
user_id = response[0]
auth_token = response[1]
print(auth_token)

header_dict = {}
header_dict['Accept'] = 'application/json'
header_dict['Accept-Language'] = 'en-US'
header_dict['Accept-Encoding'] = 'gzip, deflate'
header_dict['DNT'] = '1'
header_dict['X-Livemocha-Product'] = 'Product:Aria'
header_dict['Content-Type'] = 'application/json'
header_dict['Authorization'] = 'Bearer ' + auth_token

#data_dict = {}
data_json = {'activitySetIds': '4c9de789-5663-4c7f-9553-9f90fb156b83'}

#payload = urlencode(data_dict)
response = requests.post(url + endpoint, data=data_json, headers=header_dict)

# Process and format the response
results = json.loads(response.text)
pretty = json.dumps(results, indent=2)
print(pretty)


