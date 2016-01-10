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
img_new = img_original.resize((basewidth,hsize), PIL.Image.ANTIALIAS)

# Save the resized image and show it
img_new.save('ScaledShip.jpg')
img_new.show()