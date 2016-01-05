#tuna = int(input("What's your favorite number?\n"))
# throws exception if string entered
#print(tuna)

while True:
    try:
        number = int(input("What's your favorite number?\n"))
        print(18/number)
        break
    except ValueError:
        print("Make sure and enter a number.")
    except ZeroDivisionError:
        print("Can't enter zero.")
    # Not a good idea
    except:
        break
    finally:
        print("loop is complete")
