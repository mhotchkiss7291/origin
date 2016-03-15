from urllib import urlencode

import requests
import json
import PythonTokenGenerator

# curl \
# 'https://biblio-qa2.dev.rosettastone.com/content/productCourseSummary/Product:Aria/en-US?includeVersion=true' \
# -H 'X-Livemocha-Product: Product:Aria' \
# -H 'Content-Type: application/json;charset=UTF-8' \
# -H 'Authorization: Bearer 43f0ffeb-1592-4060-a2c2-cf2ad5183d33' \
# -H 'Origin: https://aebeditor-qa2.dev.rosettastone.com' \
# -H 'Accept-Encoding: gzip, deflate, sdch' \
# -H 'Accept-Language: en-US,en;q=0.8' \
# -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36' \
# -H 'Accept: application/json, text/plain, */*' \
# -H 'Referer: https://aebeditor-qa2.dev.rosettastone.com/' \
# -H 'X-Requested-With: Ceres' \
# -H 'Connection: keep-alive' \
# --compressed

# Strings
url = "https://biblio-qa1.dev.rosettastone.com"
endpoint = "/content/productCourseSummary/Product:Aria/en-US?includeVersion=true"

# For auth_token
user = "mhotchkiss-qa1@rosettastone.com"
environment = "qa1"
response = PythonTokenGenerator.get_token(environment, user)
user_id = response[0]
auth_token = response[1]
print('auth_token = ' + auth_token + '\n')
print('user_id = ' + user_id + '\n')

header_data = {
    'X-Livemocha-Product': 'Product:Aria',
    'Content-Type': 'application/json;charset=UTF-8',
    'Authorization': 'Bearer ' + auth_token,
    'Origin': 'https://aebeditor-qa1.dev.rosettastone.com',
    'Accept-Encoding': 'gzip, deflate, sdch',
    'Accept-Language': 'en-US,en;q=0.8',
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36',
    'Accept': 'application/json, text/plain, */*',
    'Referer': 'https://aebeditor-qa1.dev.rosettastone.com/',
    'X-Requested-With': 'Ceres',
    'Connection': 'keep-alive'
}

response = requests.get(url + endpoint + user_id, headers=header_data)

# Process and format the response
results = json.loads(response.text)
pretty = json.dumps(results, indent=2)
print(pretty)
