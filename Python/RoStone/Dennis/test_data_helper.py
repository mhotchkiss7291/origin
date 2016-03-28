from datetime import datetime, timedelta
from app_helper import data, dict_body_for, dict_to_json
from prism_api_helper import exported_job_get, exported_progress_post, exported_usage_post
from tully_api_helper import tully_auth_with

# this helper will return a SQL formatted date for yesterday's date
def sql_date(date_object=None):
    if date_object is None: date_object = (datetime.utcnow().date() + timedelta(days=-1))
    return date_object.strftime('%Y-%m-%d')
    

def sql_date_to_list(date_string):
    date_list = date_string.split('-')
    for index, item in enumerate(date_list): date_list[index] = int(item)
    return date_list


def factory_json(factory, attributes={}):
    factory = factory_dict(factory)
    factory.update(attributes)
    return dict_to_json(factory)


def factory_dict(factory):
    factory_object = factory.stub()
    return factory_object.__dict__


# this helper is for supporting any tests needing to kick off a an exported job for progress or usage reports
def generate_exported_job(by_report_type, organization_id, product_id, start_date=sql_date(), end_date=sql_date(), get_job=False):
    auth_token = tully_auth_with(data.super_organization_admin)
    request_data = dict_to_json({'organization': organization_id, 'product': product_id, 'start': start_date, 'end': end_date})
    request_headers = {'Authorization': '{}'.format(auth_token), 'Content-Type': 'application/json'}
    request_options = {'request_data': request_data, 'request_headers': request_headers}
    if by_report_type is 'progress': response = exported_progress_post(request_options)
    elif by_report_type is 'usage': response = exported_usage_post(request_options)
    if get_job is True:
        response_dict = dict_body_for(response)
        job_id = response_dict.get('jobId')
        request_options = {'job_id': job_id, 'request_headers': request_headers}
        response = exported_job_get(request_options)
    return dict_body_for(response)
    

# this helper is for getting the job status for the exported progress and usage tests validating job status after request    
def get_exported_job_status_for(job_id):
    auth_token = tully_auth_with(data.super_organization_admin)
    exported_job = dict_body_for(exported_job_get({'job_id': job_id, 'request_headers': {'Authorization': '{}'.format(auth_token)}}))
    return exported_job['status']

    