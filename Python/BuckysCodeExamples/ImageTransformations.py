import PIL
from PIL import Image

ship = Image.open("CruiseShip600x400.jpg")
ship.show()

# Set long edge maximum
basewidth = 400

# Measure and calculate aspect ratio
img_original = Image.open('CruiseShip600x400.jpg')
wpercent = (basewidth/float(img_original.size[0]))
hsize = int((float(img_original.size[1])*float(wpercent)))

# Resize the image maintaining the aspect ratio and antialias the image
img_resized = img_original.resize((basewidth,hsize), PIL.Image.ANTIALIAS)

# Save the resized image and show it
# img_resized.save('ScaledShip.jpg')
img_resized.show()

# Flip left/right
img_flippedLeftRight = img_original.transpose(Image.FLIP_LEFT_RIGHT)
img_flippedLeftRight.show()

# Rotate 90
img_rotate90 = img_original.transpose(Image.ROTATE_90)
img_rotate90.show()

