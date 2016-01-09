from PIL import Image

img = Image.open("CruiseShip.jpg")

# a 4 item tuple
area = (200, 200, 600, 775)

# Crop the area
cropped_image = img.crop(area)

# On my Mac, the big image comes up
# before the cropped image does,
# consistently using Preview
cropped_image.show()
img.show()
