from PIL import Image

ship = Image.open("CruiseShip.jpg")
salt_pier = Image.open("SaltPierReduced.jpg")

# Split the RGB channel data
r1, g1, b1 = ship.split()
r2, g2, b2 = salt_pier.split()

#print(r1)

# I don't know why this does not work
new_img = Image.merge("RGB", (r1, g2, b1) )


