class Parent():

    def print_last_name(self):
        print('Roberts')

class Child(Parent):

    def print_first_name(self):
        print('Mike')

    # Overriding function
    def print_last_name(self):
        print('Marx')

mike = Child()
mike.print_first_name()
mike.print_last_name()