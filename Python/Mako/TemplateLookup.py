from mako.template import Template
from mako.lookup import TemplateLookup

# Set top level directory to lookup from
mylookup = TemplateLookup(directories=['./mako_data'])

# Output the contents of ./mako_data/header.txt, two newlines and "hello world!"
# by using a lookup directory object
mytemplate = Template("""<%include file="header.txt"/> \n\n hello world!""", lookup=mylookup)

# Render the template
print(mytemplate.render())
