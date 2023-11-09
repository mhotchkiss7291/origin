from selenium import webdriver
from selenium.webdriver.common.keys import Keys

# Create a new instance of the Chrome driver
driver = webdriver.Chrome()

# Open Google in the browser
driver.get("https://www.google.com")

# Find the search input element by name (you can inspect the HTML to confirm the element's attributes)
search_box = driver.find_element_by_name("q")

# Type "today's news" into the search box and press Enter
search_box.send_keys("today's news")
search_box.send_keys(Keys.RETURN)

# You can also use search_box.submit() instead of sending RETURN key

# Wait for a few seconds (you can use explicit or implicit waits here)
# For demonstration purposes, we'll use a simple time.sleep
import time
time.sleep(5)

# Close the browser
driver.quit()