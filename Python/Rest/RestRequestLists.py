import requests
import json
from urllib.request import urlopen

url = 'http://restcountries.eu/rest/v1/name/norway'

# Returns a List not JSON
r = requests.get(url)

# Return the first element in the List
obj = json.loads(r.text)[0]

print(json.dumps(obj))

print(obj["capital"])



