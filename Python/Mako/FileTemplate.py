from mako.template import Template

mytemplate = Template(filename='template_data.txt')
print(mytemplate.render())

#mytemplate2 = Template(filename='template_data.txt', module_directory='mako_data/my_module_template.txt')
#print(mytemplate2.render())