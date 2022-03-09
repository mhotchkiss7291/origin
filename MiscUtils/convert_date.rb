it 'get now arithmetic exercise' do

    time_now = Time.now
    datetime_now = DateTime.now

    datetime_converted = DateTime.parse(time_now.to_s)
    time_converted = Time.parse(datetime_now.to_s)

    # Subtract two days from now and
    # return in Time format
    two_days_ago_datetime = DateTime.now - 2

end
