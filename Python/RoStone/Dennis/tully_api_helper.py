import json
import requests
from app_helper import config

def tully_auth(username, password):
    header = {'Content-Type': 'application/x-www-form-urlencoded'}
    payload = {'grant_type': 'password', 'username': username, 'password': password,
               'client_id': 'client.siren', 'client_secret': ''}
    response = requests.post(config.tully_base_url + '/oauth/token', data=payload, headers=header)
    try:
        token = json.loads(response.text)['access_token']
        return ' '.join(['Bearer', token])
    except: return('ERROR: could not successfully authenticate with user by username "{}"'.format(username))


def tully_auth_with(user):
    return tully_auth(user.username, user.password)


def get_user_id_from(auth_token):
    token_id = auth_token.rsplit('Bearer ')[1]
    response = requests.get(config.tully_base_url + '/authentication/oauth/tokens/{}'.format(token_id))
    token_dict = json.loads(response.text)
    return token_dict.get('userId')
    
    
    
    
