from PIL import Image

ship = Image.open("CruiseShip600x400.jpg")
ship.show()

turtle = Image.open("Turtle600x400.jpg")
turtle.show()

# Split the RGB channel data
r1, g1, b1 = ship.split()
print(r1, g1, b1)
r2, g2, b2 = turtle.split()
print(r2, g2, b2)

scramble_channel_ship = Image.merge('RGB', (g1, b1, r1) )
scramble_channel_ship.show()

scramble_channel_turtle = Image.merge('RGB', (g2, b2, r2) )
scramble_channel_turtle.show()

scrambled_images1 = Image.merge('RGB', (r2, g1, b2) )
scrambled_images1.show()

scrambled_images2 = Image.merge('RGB', (r1, g2, b1) )
scrambled_images2.show()



