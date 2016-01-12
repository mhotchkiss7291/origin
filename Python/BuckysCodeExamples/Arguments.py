# Kind of like overloading

def dumb_sentence(name='Mark', action='ate', item='tuna'):
    print(name, action, item)


dumb_sentence()
dumb_sentence("Jim", "runs", "fast")
dumb_sentence(item='awesome', action='is')


# Flexible number of arguments
# args is a convention in Python, not a keyword
def add_numbers(*args):
    total = 0
    for a in args:
        total += a
    print("Sum = ", total)


add_numbers()
add_numbers(3, 4)
add_numbers(6, 7, 8, 9)


def health_calculator(age, apples_ate, cigs_smoked):
    answer = (100 - age) + (apples_ate * 3.5) - (cigs_smoked * 2)
    print("Age Expectancy = ", answer)


marks_data = [27, 20, 0]
grouchos_data = [100, 10, 1000]

# instead of
# health_calculator(marks_data[0], marks_data[2], marks_data[3] )
# Unpacks arguments
health_calculator(*marks_data)
health_calculator(*grouchos_data)
