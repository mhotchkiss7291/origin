import os
import yaml
from collections import namedtuple
from mako.template import Template

CONFIG = yaml.load(Template(filename='config.yml').render())
DATA = yaml.load(open(os.path.join(os.path.dirname(__file__), 'data.yml')))


class App(object):
    class Config(object):

        def __init__(self):
            pass

        def __getattr__(self, key):
            try:
                return self.config_with_environment()[str(key)]
            except Exception, error:
                return str(error)

        def config_with_environment(self):
            CONFIG.update(self.environment_config())
            return CONFIG

        def environment_config(self):
            if self.specified_environment() is None: raise 'Must specify test environment with TEST_ENV=<test_environment>!'
            if isinstance(CONFIG[self.specified_environment()],
                          dict) is False: raise 'Must specify valid test environment!'
            return CONFIG[self.specified_environment()]

        def specified_environment(self):
            return os.getenv('TEST_ENV') or CONFIG['default_test_environment']

    class Data(object):

        def __init__(self):
            pass

        def __getattr__(self, key):
            try:
                data_dict = DATA[str(key)]
                return namedtuple('data', data_dict.keys())(*data_dict.values())
            except Exception, error:
                return str(error)
