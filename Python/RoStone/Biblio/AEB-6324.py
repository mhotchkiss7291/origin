import requests
import json
from PythonTokenGenerator import get_token

url = "https://biblio-qa1.dev.rosettastone.com"
end_point = "/schedule/join/attendee?meetingID=testmeeting&host=rosettastoneqadev.adobeconnect.com"
user = "mhotchkiss@rosettastone.com"
environment = "qa1"

# Get auth_token
response = get_token(environment, user)
user_id = response[0]
auth_token = response[1]

# Start Dan code
data = {}
header_dict = {}

header_dict['Authorization: Bearer'] = auth_token
header_dict['X-Rosettastone-Product-Id'] = product.82692f39-a40e-4051-a6dc-e0cbf027c80b


#data['grant_type'] = grant
#data['client_id'] = client_id
## End of Dan code
#
#headers = {
#    'X-Rosettastone-Product-Id: product.82692f39-a40e-4051-a6dc-e0cbf027c80b',
#    'X-Livemocha-Product: Product:Aria'
#}
#
#header_dict['Host'] = "tully-{}.dev.rosettastone.com".format(env)
#
## This isn't working yet
#response = requests.post(endpoint, data=payload, headers=header_dict)
#
##rest_response = requests.get(url + end_point + user_id, headers=headers)
##results = json.loads(rest_response.text)
##print(json.dumps(results))
#
#
# client_id = 'siren'
#    grant = 'password'
#    #user_id = 'mhotchkiss@rosettastone.com'
#    user_pw = 'password'
#    if env == "yrod" or env == "prod-wdc":
#        user_id = "dadams+soa_production@rosettastone.com"
#        password = "password"
#    refresh_token = None
#
#    endpoint = ""
#    if "prod" in env.lower():  # 1621d227-1105-4939-ba4d-d2d53c5cd5eb
#        endpoint = "https://tully.rosettastone.com/oauth/token"
#    else:
#        endpoint = "https://tully-{}.dev.rosettastone.com/oauth/token".format(env)
#    print
#    endpoint
#
#    header_dict['Host'] = "tully-{}.dev.rosettastone.com".format(env)
#    header_dict['X-Livemocha-Product'] = 'Product:Aria'
#    header_dict['Referer'] = 'https://' + 'livemocha_client'
#
#    if refresh_token:
#        data['refresh_token'] = refresh_token
#    else:
#        data['username'] = user_id
#        data['password'] = user_pw
#
#    payload = urlencode(data)
#
#    response_string = json.loads(response.text)
#    #print(response_string)
#
#    userId = response_string.get("userId")
#    token = response_string.get("access_token")
#
#    return (userId, token)