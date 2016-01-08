from PIL import Image

ship = Image.open("CruiseShip.jpg")

# Is it RGB? Yes
print(ship.mode)

# Split the color channels into
# a tuple
r, g, b = ship.split()

# Display each of the channel tuples
# in its own image
r.show()
g.show()
b.show()
