import requests
from app_helper import config

LPS_URL = config.lps_base_url

def lps_get(request_endpoint, options={}):
    return requests.get(LPS_URL + request_endpoint, **options)


def lps_post(request_endpoint, options={}):
    return requests.post(LPS_URL + request_endpoint, **options)
    
    
# LaunchPad Service APIs

def lps_reporting_pretest_results_post(options={}):
    query_params = options.get('query_params')
    request_headers = options.get('request_headers')
    request_options = {'params': query_params, 'headers': request_headers}
    return lps_post('/reporting/pretest_results', request_options)