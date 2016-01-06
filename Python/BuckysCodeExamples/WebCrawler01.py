import requests
from bs4 import BeautifulSoup

def trade_spider(max_pages):
    page = 1
    while page < max_pages:

        # url = 'https://buckysroom.org/trade/search.php?page=' + str(page)
        url = 'https://github.com/mhotchkiss7291/origin/tree/master/Python/BuckysCodeExamples'

        source_code = requests.get(url)
        plain_text = source_code.text
        soup = BeautifulSoup(plain_text, "html.parser")
        for link in soup.findAll('a', {'class': 'js-directory-link js-navigation-open'}):
            href = link.get('href')
            title = link.string
            print(href)
            print(title)
        page += 1

trade_spider(2)
