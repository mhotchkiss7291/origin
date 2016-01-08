from PIL import Image

img = Image.open("TestImage-1.jpg")

# a 4 item tuple
area = (100, 100, 300, 375)

# Crop the area
cropped_image = img.crop(area)

img.show()
cropped_image.show()