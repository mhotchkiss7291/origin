from PIL import Image

# Identify two images
boat = Image.open("CruiseShipFull.jpg")
salt_pier = Image.open("SaltPierReduced.jpg")

# Where to place the image? 20 pixels inside the
# top left corner of the base layer image
upperLeftAnchor = (20, 20)

boat.show()
salt_pier.show()

# Paste one image on another
boat.paste(salt_pier, upperLeftAnchor)
boat.show()


