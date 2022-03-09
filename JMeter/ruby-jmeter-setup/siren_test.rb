require 'ruby-jmeter'

test do

  # Constants
  test_env = ENV['TEST_ENV']
  if test_env.nil?
    raise 'ERROR: you must set TEST_ENV'
  end

  # JMeter test parameters
  thread_count = ENV['THREAD_COUNT'] ? ENV['THREAD_COUNT'].to_i : 10
  pause_value = ENV['PAUSE'] ? ENV['PAUSE'].to_i : 300
  thread_loop = ENV['LOOPS'] ? ENV['LOOPS'].to_i : 1
  thread_rampup = ENV['RAMPUP'] ? ENV['RAMPUP'].to_i : 1

  # Thread Group
  threads name: 'Siren',
          count: thread_count,
          loop: thread_loop,
          rampup: thread_rampup do

    # Login
    simple_controller name: 'Login' do
      endpoint_url = 'https://tully-' + test_env + '.dev.rosettastone.com/oauth/token'
      header [
                 {name: 'Content-Type', value: 'application/x-www-form-urlencoded'}
             ]
      post name: 'LoginRequest',
           url: endpoint_url ,
           raw_body: 'grant_type=password&username=mrh-admin@rosettastone.com&password=password&client_id=client.scholar.mobile'
      extract regex: '"access_token":"(.+?)"', name: 'access_token'
      extract regex: '"userId":"(.+?)"', name: 'user_id'
      extract regex: '"organizationId":"(.+?)"', name: 'org_id'
      think_time pause_value, pause_value
    end

    # Get Products
    simple_controller name: 'GetProducts' do
      endpoint_url = 'https://siren-' + test_env + '.dev.rosettastone.com'
      path = '/api/products?expanded=true'
      header [
                 {name: 'Authorization', value: 'Bearer ${access_token}'}
             ]
      get name: 'ProductRequest',
           url: endpoint_url + path
      think_time pause_value, pause_value
    end

    # Get Organization Entitlements
    simple_controller name: 'GetOrgEntitlements' do
      endpoint_url = 'https://siren-' + test_env + '.dev.rosettastone.com'
      path = '/api/organizations/${org_id}/search/entitlements/product'
      header [
                  {name: 'Authorization', value: 'Bearer ${access_token}'},
                  {name: 'Content-Type', value: 'application/json'}
             ]
      post name: 'EntitlementRequest',
          url: endpoint_url + path,
          raw_body: '{"type":"COURSE_BUNDLE"}'
      think_time pause_value, pause_value
    end

    # Log results into a CSV file
    #view_results_in_table name: 'GaiaServerResults', filename: './siren-server.csv'

  end
end.jmx
