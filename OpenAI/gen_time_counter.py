
import datetime

# function to calculate the time difference between two dates
def calculate_time_difference(start_date, end_date):
    time_difference = end_date - start_date
    years = time_difference.days // 365
    days = time_difference.days % 365
    minutes = time_difference.seconds // 60
    seconds = time_difference.seconds % 60
    return years, days, minutes, seconds

# get the current date and time
now = datetime.datetime.now()

# specify a date in the past
past_date = datetime.datetime(2000, 1, 1, 0, 0, 0)

# calculate the time difference
years, days, minutes, seconds = calculate_time_difference(past_date, now)
# output the result
print("Years: ", years)
print("Days: ", days)
print("Minutes: ", minutes)
print("Seconds: ", seconds)