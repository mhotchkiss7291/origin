#item = ['January 5, 2016', 'Bread Gloves', 8.51]

# Too much work
# date = item[0]

date, name, price = ['January 5, 2016', 'Bread Gloves', 8.51]
print(name)

# python cookbook

# Different length lists

# Unpack the list
# Get all stuff in the middle no matter its size
# Notice the *middle item
def drop_first_last(grades):

    first, *middle, last = grades
    avg = sum(middle) / len(middle)
    print(avg)

drop_first_last([65, 76, 98, 54, 12])
drop_first_last([65, 76, 98, 99, 97, 87, 30, 54, 12])
