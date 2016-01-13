import requests
import json

#url = "https://github.com/mhotchkiss7291/origin"
#url = "https://developer.github.com/v3/activity/events/#list-public-events"
url = "http://jsonplaceholder.typicode.com/"

response1 = requests.get(url)
#print( "Status Code = " + str(response1.status_code))

parsed_json = json.loads(response1)

print(json.dumps(parsed_json))

