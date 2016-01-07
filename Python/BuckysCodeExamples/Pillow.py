from PIL import Image

img = Image.open("TestImage-1.jpg")
print(img.size)
print(img.format)

# Open in the default viewer with a temporary name
img.show()