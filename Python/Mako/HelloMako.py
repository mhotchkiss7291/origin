from mako.template import Template

mytemplate = Template("hello world!")
print(mytemplate.render())

mytemplate = Template("hello, ${name}!")
print(mytemplate.render(name="jack"))