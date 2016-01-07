import requests
from bs4 import BeautifulSoup

import operator

def start(url):

    # Make a soup for bs4 to crawl through html
    word_list = []
    source_code = requests.get(url).text
    soup = BeautifulSoup(source_code, "html.parser")

    # https://github.com/mhotchkiss7291/origin
    # class: js-directory-link js-navigation-open

    # Find in the soup the text of the main links of the folders on Marks Git repository
    for post_text in soup.find_all('a', {'class': 'js-directory-link js-navigation-open'}):

        # Just get the text on the screen
        content = post_text.string

        # All lower case words and splits by spaces
        words = content.lower().split()

        # Get individual lower case words
        for each_word in words:
            word_list.append(each_word)

     clean_up_list(word_list)

def clean_up_list(word_list):

    clean_word_list = []

    for word in word_list:

        # Big symbols string that needs to be preserved
        # Symbols that need to be ignored in the search
        symbols = "!@#$%^&*()_+{}:\"<>?,./;'[]-='"

        for i in range(0, len(symbols))
            word = word.r



# URL to call
url = 'https://github.com/mhotchkiss7291/origin'

# run
start(url)


