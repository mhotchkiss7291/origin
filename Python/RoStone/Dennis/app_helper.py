import json
import urllib
from app import App

# initilize the config and data objects
config = App.Config()
data = App.Data()


def where_am_i():
    return config.specified_environment()


whereami = where_am_i()


def dict_body_for(response):
    return json.loads(response.text)


def dict_to_json(dict):
    return json.dumps(dict)


def url_encode(string):
    return urllib.quote_plus(string)


# simple class for creating objects
class Struct(object):
    def __init__(self, **entries):
        self.__dict__.update(entries)
