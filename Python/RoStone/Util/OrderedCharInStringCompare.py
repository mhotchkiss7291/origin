# This was a challenge that I had in an interview for a general developer in the Test
# environment. I did not succeed in meeting this challenge on the white board.
#  I said I would need some time to consider the problem with experimentation in an IDE.

# This is not a literal transcription but the gist of the challenge.
# "Given two strings, what would be 'the most efficient way' to sort two strings for
# unique size and natural ordered character list to determine their equality?"

# I don't know whether this is "the most efficient" , but here is my candidate:


str1 = input("Enter the string you want to check for equal size and ordered characters: ")
str2 = input("Enter the string to compare: ")

# I think that this is a contradiction to Java/vs/Python in operators.
# Let me explain:
# The == operator in Java points the first argument object to th second argument object
# This effectively points to objects in memory
# But in the following code, in Java, you would need the ".equals()" method implemented
# and testable.

# This "equivalence" coding is still confusing to me. Java/Python

if sorted(str1) == sorted(str2):
        print ("Size and char order match!")
else:
        print ("Not the same")

# How could this be done better?
