import requests
from app_helper import config

PRISM_URL = config.prism_base_url

def prism_get(request_endpoint, options={}):
    return requests.get(PRISM_URL + request_endpoint, **options)


def prism_post(request_endpoint, options={}):
    return requests.post(PRISM_URL + request_endpoint, **options)


def prism_put(request_endpoint, options={}):
    return requests.put(PRISM_URL + request_endpoint, **options)


def prism_delete(request_endpoint, options={}):
    return requests.delete(PRISM_URL + request_endpoint, **options)

    
# Exported API

def exported_get(options={}):
    request_headers = options.get('request_headers')
    request_options = {'headers': request_headers}
    return prism_get('/exported/{asset_key_id}'.format(**options), request_options)
    

def exported_job_get(options={}):
    request_headers = options.get('request_headers')
    request_options = {'headers': request_headers}
    return prism_get('/exported/job/{job_id}'.format(**options), request_options)

    
def exported_progress_post(options={}):
    request_data = options.get('request_data')
    request_headers = options.get('request_headers')
    request_options = {'data': request_data, 'headers': request_headers}
    return prism_post('/exported/progress', request_options)
    

def exported_usage_post(options={}):
    request_data = options.get('request_data')
    request_headers = options.get('request_headers')
    request_options = {'data': request_data, 'headers': request_headers}
    return prism_post('/exported/usage', request_options)
    

# Learner Learning Levels API

def learnerlearninglevels_get(options={}):
    request_headers = options.get('request_headers')
    request_options = {'headers': request_headers}
    return prism_get('/learnerlearninglevels/organization/{organization_id}/product/{product_id}'.format(**options), request_options)
    

# Learner Usage Checker API

def learnerusagechecker_get(options={}):
    query_params = options.get('query_params')
    request_headers = options.get('request_headers')
    request_options = {'params': query_params, 'headers': request_headers}
    return prism_get('/learnerusagechecker/organization/{organization_id}/product/{product_id}'.format(**options), request_options)
    

# Organization Learning Levels API

def organizationlearninglevels_get(options={}):
    request_headers = options.get('request_headers')
    request_options = {'headers': request_headers}
    return prism_get('/organizationlearninglevels/organization/{organization_id}/product/{product_id}'.format(**options), request_options)


# Service Ready, Status & Version APIs

def serviceready_get(options={}):
    request_headers = options.get('request_headers')
    request_options = {'headers': request_headers}
    return prism_get('/serviceready', request_options)


def status_get():
    return prism_get('/status')
    
    
def status_configuration_paths_get():
    return prism_get('/status/configuration_paths')
    
    
def status_health_check_get():
    return prism_get('/status/health_check')
    
    
def status_properties_get():
    return prism_get('/status/properties')
    
    
def version_get():
    return prism_get('/version')


# Single Solution API

def singlesolution_pretestresults_get(options={}):
    query_params = options.get('query_params')
    request_headers = options.get('request_headers')
    request_options = {'params': query_params, 'headers': request_headers}
    return prism_get('/singleSolution/preTestResults/{organization_id}'.format(**options), request_options)
    

def singlesolution_pretestresults_post(options={}):
    request_data = options.get('request_data')
    request_headers = options.get('request_headers')
    request_options = {'data': request_data, 'headers': request_headers}
    return prism_post('/singleSolution/preTestResults', request_options)


# Usage API

def usage_product_user_get(options={}):
    query_params = options.get('query_params')
    request_headers = options.get('request_headers')
    request_options = {'params': query_params, 'headers': request_headers}
    return prism_get('/usage/product/{product_id}/user/{user_id}'.format(**options), request_options)

