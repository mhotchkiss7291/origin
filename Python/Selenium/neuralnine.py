from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager

options = Options()
options.add_experimental_option("detach", True)

driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()),
                          options=options)

driver.get("https://neuralnine.com/")
driver.maximize_window()

# Get all of the links with an innerHTML type
links = driver.fine_elements("xpath", "//a[@href]")
for link in links:
    print(link)