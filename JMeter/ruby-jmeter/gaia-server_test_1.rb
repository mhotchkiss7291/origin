require 'ruby-jmeter'

test do

  # Constants
  test_env = 'qa5'
  endpoint_url = 'https://gaia-server-' + test_env + '.dev.rosettastone.com'
  thread_count = 100
  thread_loop = 10
  thread_rampup = 1
  user_email = 'mrh-catalyst@rosettastone.com'
  password = 'password'
  course_id = 'c11c7c3947eba838810a81f62b736b9f'
  assigned_course_id = 'd5ff4823b4d770eab43f47fd7e1fb139'
  sequence_id = '17ba3cc1-5f77-4a58-b32a-c7e0e6b62edd'

  # Thread Group
  threads name: 'Gaia Users',
          count: thread_count,
          loop: thread_loop,
          rampup: thread_rampup do

    # Login
    simple_controller name: 'LoginController' do
      path = '/login'
      header [
                 {name: 'accept', value: 'application/json'},
                 {name: 'content-type', value: 'application/x-www-form-urlencoded'}
             ]
      post name: 'LoginRequest',
           url: endpoint_url + path,
           raw_body: 'username=' + user_email + '&password=' + password
      extract regex: '"access_token":"(.+?)"', name: 'access_token'
      extract regex: '"userId":"(.+?)"', name: 'user_id'
    end

    # Validate Token
    simple_controller name: 'ValidateTokenController' do
      path = '/validateToken'
      header [
                 {name: 'authorization', value: 'Bearer ${access_token}'}
             ]
      get name: 'ValidateTokenRequest',
          url: endpoint_url + path
      assert contains: '${access_token}'
      assert contains: '${user_id}'
    end

    # Versions
    simple_controller name: 'VersionsController' do
      path = '/graphql'
      header [
                 {name: 'authorization', value: 'Bearer ${access_token}'},
                 {name: 'accept', value: 'application/json'},
                 {name: 'content-type', value: 'application/json'},
             ]
      post name: 'VersionsRequest',
           url: endpoint_url + path,
           raw_body: '{"query":"{versions {id build timestamp}}","variables":null,"operationName":null}'
      assert contains: 'data'
      assert contains: 'Gaia Server'
      assert contains: 'ProgressTracker'
      assert contains: 'ExpertGrading'
    end

    # Available Courses
    simple_controller name: 'AvailableCoursesController' do
      path = '/graphql'
      header [
                 {name: 'authorization', value: 'Bearer ${access_token}'},
                 {name: 'accept', value: 'application/json'},
                 {name: 'content-type', value: 'application/json'},
             ]
      post name: 'AvailableCoursesRequest',
           url: endpoint_url + path,
           raw_body: '{"query":"{availableCourses(userId:\"${user_id}\")' +
               '{id courseId title images {id type images {id type media_uri }}}}",' +
               '"variables":null,"operationName":null}'
      assert contains: 'data'
      assert contains: 'availableCourses'
      assert contains: course_id
    end

    # Get Assigned Courses
    simple_controller name: 'GetAssignedCoursesController' do
      path = '/graphql'
      header [
                 {name: 'authorization', value: 'Bearer ${access_token}'},
                 {name: 'accept', value: 'application/json'},
                 {name: 'content-type', value: 'application/json'},
             ]
      post name: 'AvailableCoursesRequest',
           url: endpoint_url + path,
           raw_body: '{"query":"{assignedCourses(userId: \"${user_id}\")' +
               ' {id, title}}","variables":null,"operationName":null}'
      assert contains: 'data'
      assert contains: 'assignedCourses'
      assert contains: assigned_course_id
    end

    # Get Sequences
    simple_controller name: 'GetSequenceController' do
      path = '/graphql'
      header [
                 {name: 'authorization', value: 'Bearer ${access_token}'},
                 {name: 'accept', value: 'application/json'},
                 {name: 'content-type', value: 'application/json'},
             ]
      post name: 'GetSequenceRequest',
           url: endpoint_url + path,
           raw_body: '{"query":"{sequence(courseId: \"' + course_id + '\", ' +
               'sequenceId: \"' + sequence_id + '\", ' +
               'userId: \"${user_id}\") ' +
               '{id images {id images {id type media_uri}}}}","variables":null,"operationName":null}'
      assert contains: 'data'
      assert contains: 'sequence'
      assert contains: sequence_id
    end

    # Get Progress
    simple_controller name: 'GetProgressController' do
      path = '/graphql'
      header [
                 {name: 'authorization', value: 'Bearer ${access_token}'},
                 {name: 'accept', value: 'application/json'},
                 {name: 'content-type', value: 'application/json'},
             ]
      post name: 'GetProgressRequest',
           url: endpoint_url + path,
           raw_body: '{"query":"{progress(' +
               'userId: \"${user_id}\") ' +
               '{id courseId endTimestamp durationMs bestGrade latestGrade percentComplete sequences ' +
               '{id sequenceId endTimestamp durationMs bestGrade latestGrade percentComplete activities ' +
               '{id activityId durationMs endTimestamp bestGrade latestGrade percentComplete attempts ' +
               '{id activityAttemptId durationMs endTimestamp bestGrade latestGrade percentComplete steps ' +
               '{id activityStepId endTimestamp durationMs bestScore latestScore attempts ' +
               '{id activityStepAttemptId userAgent score durationMs answers ' +
               '{id answerNumber answer' +
               '}}}}}}}}","variables":null,"operationName":null}'
      assert contains: 'data'
      assert contains: 'progress'
    end

    # Log results into a CSV file
    view_results_in_table name: 'GaiaServerResults', filename: './gaia-server.csv'

  end
end.jmx
