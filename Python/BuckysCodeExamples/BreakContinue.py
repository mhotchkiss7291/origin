magicNumber = 26

# Comment here

'''
for n in range(101):
    if n is magicNumber:
        print()
'''

for n in range(101):
    if n is magicNumber:
        print(n, " is the magicNumber!")
        break
    else:
        print(n)

# Print a number and a string
print(9, " Bucky" + " Horse")

# Continue
numbersTaken = [2,5,12,33,17]
print("Numbers available:")
for n in range(1, 20):
    if n in numbersTaken:
        continue
    print(n)