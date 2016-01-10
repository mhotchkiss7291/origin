from PIL import Image
from PIL import ImageFilter

img_original = Image.open("CruiseShip600x400.jpg")
#img_original.show()

# Black & White
img_bw = img_original.convert("L")
img_bw.show()

# Blur
img_blur = img_original.filter(ImageFilter.BLUR)
img_blur.show()

# Detail
img_detail = img_original.filter(ImageFilter.DETAIL)
img_detail.show()

# Edges
img_edges = img_original.filter(ImageFilter.FIND_EDGES)
img_edges.show()

