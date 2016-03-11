from urllib import urlencode

import requests
import json
import PythonTokenGenerator

# url \
# "https://orcabe-qa1.dev.rosettastone.com/orca/users/c6e3f3dd-b9dd-4c98-96a1-126190ec4927" \
# -H "Authorization: Bearer acc5f53e-7048-4ea1-a611-48e0500c4da7" \
#  | python -mjson.tool

# Strings
url = "https://orcabe-qa1.dev.rosettastone.com"
endpoint = "/orca/users/"

# For auth_token
user = "mhotchkiss-qa1@rosettastone.com"
environment = "qa1"
response = PythonTokenGenerator.get_token(environment, user)
user_id = response[0]
auth_token = response[1]
print('auth_token = ' + auth_token + '\n')
print('user_id = ' + user_id + '\n')

header_data = {
    'Authorization': 'Bearer ' + auth_token
}

response = requests.get(url + endpoint + user_id, headers=header_data)

# Process and format the response
results = json.loads(response.text)
pretty = json.dumps(results, indent=2)
print(pretty)

#Results

# {
#   "guid": "c6e3f3dd-b9dd-4c98-96a1-126190ec4927",
#   "href": "http://orcabe-qa1.dev.rosettastone.com:80/orca/users/c6e3f3dd-b9dd-4c98-96a1-126190ec4927",
#   "products": [
#     {
#       "courses": [
#         "course.product.aeb.1",
#         "course.product.aeb.2"
#       ],
#       "guid": "product.aeb",
#       "name": "Product product.aeb"
#     },
#     {
#       "courses": [
#         "course.product.rsa.1",
#         "course.product.rsa.2"
#       ],
#       "guid": "product.rsa",
#       "name": "Product product.rsa"
#     }
#   ]
# }
