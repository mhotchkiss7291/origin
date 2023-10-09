from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from webdriver_manager.chrome import ChromeDriverManager

import time

options = Options()
options.add_experimental_option("detach", True)

driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()),
                          options=options)

driver.get("https://facebook.com/")

# tag & name
#driver.find_element(By.CSS_SELECTOR, 'input#email').send_keys('abc@gmail.com')
#driver.find_element(By.CSS_SELECTOR, '#email').send_keys('abc@gmail.com')

# tag & class
#driver.find_element(By.CSS_SELECTOR, 'input.inputtext').send_keys('abc@gmail.com')
#driver.find_element(By.CSS_SELECTOR, '.inputtext').send_keys('abc@gmail.com')

# tag & attribute
#driver.find_element(By.CSS_SELECTOR, 'input[data-testid=royal_email]').send_keys('abc@gmail.com')
#driver.find_element(By.CSS_SELECTOR, '[data-testid=royal_email]').send_keys('abc@gmail.com')

# tag, class, attribute
driver.find_element(By.CSS_SELECTOR, 'input.inputtext[data-testid=royal_email]').send_keys('abc@gmail.com')
driver.find_element(By.CSS_SELECTOR, 'input.inputtext[data-testid=royal_pass]').send_keys('123')

time.sleep(3)
driver.close()
driver.quit()
