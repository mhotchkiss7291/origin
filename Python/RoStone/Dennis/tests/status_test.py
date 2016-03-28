from bootstrap import *

class TestStatus(object):
    
    def setUp(self):
        pass
        
    def tearDown(self):
        pass

    @attr('/status', 'smoke')
    def test_get_request_for_status_returns_success_response(self):
        """GET request for /status returns success response"""
        response = status_get()
        expect(response.status_code).to(equal(200))
        expect(response.text).to(be_empty)

    @attr('/status/configuration_paths', 'smoke')
    def test_get_request_for_status_configuration_paths_returns_success_response(self):
        """GET request for /status/configuration_paths returns success response"""
        response = status_configuration_paths_get()
        expect(response.status_code).to(equal(200))
        expect(response.text).to(be_empty)

    @attr('/status/health_check', 'smoke')
    def test_get_request_for_status_health_check_returns_success_response(self):
        """GET request for /status/health_check returns success response"""
        response = status_health_check_get()
        expect(response.status_code).to(equal(200))
        response_dict = dict_body_for(response)
        expect(response_dict).to(have_keys({'isUp':True, 'issue':''}))
        expect(response_dict['rttMs']).to(be_within(0, 1000))    
    
    @attr('/status/properties', 'smoke')
    def test_get_request_for_status_properties_returns_success_response(self):
        """GET request for /status/properties returns success response"""
        response = status_properties_get()
        expect(response.status_code).to(equal(200))
        expect(response.text).to(be_empty)
    
    
