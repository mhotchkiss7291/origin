first = ['Mark', 'Tom', 'Ron']
last = ['Hotchkiss', 'Hanks', 'Kneusel']

# Like zipper teeth
names = zip(first, last)

# [('Mark', 'Hotchkiss), ('Tom', 'Hanks'), ('Ron', 'Kneusel')]
for a, b in names:
    print(a, b)
