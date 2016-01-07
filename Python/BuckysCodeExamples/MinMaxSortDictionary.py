stocks = {
    'GOOG': 520.54,
    'FB': 76.45,
    'YHOO': 39.66,
    'AMZN': 306.21,
    'APPL': 99.76,
}

# Print the minimum priced stock
print("Min: ")
print(min(zip(stocks.values(), stocks.keys())))
print("\n")

print("Max: ")
print(max(zip(stocks.values(), stocks.keys())))
print("\n")

# Sorted by value
print("Sorted by value: ")
print(sorted(zip(stocks.values(), stocks.keys())))
print("\n")

# Sorted by symbol
print("Sorted by symbol: ")
print(sorted(zip(stocks.keys(), stocks.values())))

