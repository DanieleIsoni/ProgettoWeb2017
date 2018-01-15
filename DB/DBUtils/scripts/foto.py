import urllib.request
import uuid

url = 'http://lorempixel.com/600/600/cats/'
urllib.request.urlretrieve(url, "avatars/")
