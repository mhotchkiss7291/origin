'''
import random
import urllib.request

def download_web_image(url):
    name = random.randrange(1, 1000)
    full_name = str(name) + ".jpg"
    urllib.url2pathname(url)

download_web_image("https://mhotchkiss.smugmug.com/Bonaire-2015-Underwater-Only/i-RTvzh33")

import urllib.request

url = "http://www.google.com/"
request = urllib.request.Request(url)
response = urllib.request.urlopen(request)
print (response.read().decode('utf-8'))
'''
