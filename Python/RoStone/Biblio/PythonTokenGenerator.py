import json
import requests
from urllib.parse import urlencode


def get_token(env, user_id):
    #if env == "qa":
    #    env = "qa2"

    client_id = 'siren'
    grant = 'password'
    #user_id = 'mhotchkiss@rosettastone.com'
    user_pw = 'password'
    if env == "prod" or env == "prod-wdc":
        user_id = "dadams+soa_production@rosettastone.com"
        password = "password"
    refresh_token = None

    data = {}
    header_dict = {}

    header_dict['Content-Type'] = 'application/x-www-form-urlencoded;application/json'

    data['grant_type'] = grant
    data['client_id'] = client_id
    endpoint = ""
    if "prod" in env.lower():  # 1621d227-1105-4939-ba4d-d2d53c5cd5eb
        endpoint = "https://tully.rosettastone.com/oauth/token"
    else:
        endpoint = "https://tully-{}.dev.rosettastone.com/oauth/token".format(env)
    print
    endpoint

    header_dict['Host'] = "tully-{}.dev.rosettastone.com".format(env)
    header_dict['X-Livemocha-Product'] = 'Product:Aria'
    header_dict['Referer'] = 'https://' + 'livemocha_client'

    if refresh_token:
        data['refresh_token'] = refresh_token
    else:
        data['username'] = user_id
        data['password'] = user_pw

    payload = urlencode(data)
    response = requests.post(endpoint, data=payload, headers=header_dict)

    response_string = json.loads(response.text)
    #print(response_string)

    userId = response_string.get("userId")
    token = response_string.get("access_token")

    return (userId, token)

