def celsiusToFahrenheit(celsius_temp):

    fahrenheit_temp = (celsius_temp * 9 / 5) + 32
    return fahrenheit_temp

def fahrenheitToCelcius(fahrenheit_temp):

    celsius_temp = (fahrenheit_temp - 32) * 5 / 9
    return celsius_temp


print(celsiusToFahrenheit(0))
print(fahrenheitToCelcius(32))
print(celsiusToFahrenheit(100))
print(fahrenheitToCelcius(100))
