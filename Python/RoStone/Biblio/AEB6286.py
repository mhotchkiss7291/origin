from urllib import urlencode

import requests
import json
import PythonTokenGenerator

# curl \
# 'https://biblio-qa1.dev.rosettastone.com/content/productCourseSummary/Product:Aria/en-US' \
# -H 'X-Livemocha-Product: Product:Aria' \
# -H 'Authorization: Bearer 41c38eaa-4d73-4bdb-81f1-2e64385a77db' \
# | python -mjson.tool


# Strings
url = "https://biblio-qa1.dev.rosettastone.com"
endpoint = "/content/productCourseSummary/Product:Aria/en-US"

# For auth_token
user = "mhotchkiss-qa1@rosettastone.com"
environment = "qa1"
response = PythonTokenGenerator.get_token(environment, user)
user_id = response[0]
auth_token = response[1]
print('auth_token = ' + auth_token + '\n')

header_data = {
    'X-Livemocha-Product': 'Product:Aria',
    'Authorization': 'Bearer ' + auth_token
}

response = requests.get(url + endpoint, headers=header_data)

# Process and format the response
results = json.loads(response.text)
pretty = json.dumps(results, indent=2)
print(pretty)
