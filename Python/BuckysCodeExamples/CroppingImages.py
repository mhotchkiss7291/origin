from PIL import Image

img = Image.open("TestImage-4.jpg")

# a 4 item tuple
area = (200, 200, 600, 775)

# Crop the area
cropped_image = img.crop(area)

img.show()
cropped_image.show()