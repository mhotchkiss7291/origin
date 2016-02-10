from urllib import urlencode

import requests
import json
from PythonTokenGenerator import get_token

#curl \
#'https://scholar-qa2.dev.rosettastone.com/schedule/join/attendee
# ?meetingID=aeb-6324-qa2&
# host=rosettastoneqadev.adobeconnect.com
# &access_token=f60e2d38-d43a-4d45-93f7-67de408b5758' \
#--include

url = "https://biblio-qa2.dev.rosettastone.com"
endpoint = url + "/schedule/join/attendee"
meetingID = 'aeb-6324-qa2'
host = 'rosettastoneqadev.adobeconnect.com'
user = "mhotchkiss-qa2@rosettastone.com"
environment = "qa2"

print(endpoint)

# Get auth_token from user
response = get_token(environment, user)
user_id = response[0]
#auth_token = response[1]
auth_token = 'f60e2d38-d43a-4d45-93f7-67de408b5758'
print(auth_token)

# Start Dan code
data = {}
data['meetingID'] = meetingID
data['host'] = host
data['access_token'] = auth_token

# Header setting
header_dict = {}

payload = urlencode(data)

response = requests.get(endpoint, data=payload, headers=header_dict)

results = json.loads(response.text)
pretty = json.dumps(results, indent=2)
print(pretty)
