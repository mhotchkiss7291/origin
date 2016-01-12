import requests
import json

url = "https://github.com/mhotchkiss7291/origin"
#url = "https://github.com/timeline.json"

# Make a request and return a response
response1 = requests.get(url)

# Data
print( "Status Code = " + str(response1.status_code))

# Content
print(response1.json)

