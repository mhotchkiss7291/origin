import requests
from bs4 import BeautifulSoup

import operator

def start(url):
    word_list = []
    source_code = requests.get(url).text
    soup = BeautifulSoup(source_code, "html.parser")

    # https://github.com/mhotchkiss7291/origin
    # class: js-directory-link js-navigation-open

    # Find the text of the main links of the folders on Marks Git repository
    for post_text in soup.find_all('a', {'class': 'js-directory-link js-navigation-open'}):

        # Just get the text on the screen
        content = post_text.string

        # All lower case words and splits by spaces
        words = content.lower().split()

        # Get individual lower case words
        for each_word in words:
            print(each_word)
            word_list.append(each_word)


# URL to call
url = 'https://github.com/mhotchkiss7291/origin'

# run
start(url)


