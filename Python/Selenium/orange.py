from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from webdriver_manager.chrome import ChromeDriverManager

import time

#

options = Options()
options.add_experimental_option("detach", True)

driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()),
                          options=options)

driver.get("https://opensource-demo.orangehrmlive.com/")

# Doesn't work yet
#driver.find_element(By.NAME, 'username').send_keys('Admin')

time.sleep(2)
driver.close()
driver.quit()
