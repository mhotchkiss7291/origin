from urllib import request

goog_url = "http://real-chart.finance.yahoo.com/table.csv?s=GOOG&d=11&e=29&f=2015&g=d&a=2&b=27&c=2014&ignore=.csv"

def download_stock_data(csv_url):

    #Request and convert to string
    response = request.urlopen(csv_url)
    csv = response.read()
    csv_str = str(csv)

    #Parse by line
    lines = csv_str.split("\\n")

    # Raw local file name
    dest_url = r"mrh_goog.csv"

    #Open file for writing
    fx = open(dest_url, "w")

    #Write CSV data
    for line in lines:
        fx.write(line + "\n")

    fx.close()

# Run
download_stock_data(goog_url)
