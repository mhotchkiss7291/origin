class Girl:

    # class variable
    gender = 'female'

    def __init__(self, name):

        # instance variable
        self.name = name

r = Girl('Rachel')
s = Girl('Susie')
print(r.gender)
print(s.gender)
print(r.name)
print(s.name)
