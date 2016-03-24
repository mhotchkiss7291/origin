from mako.template import Template

mytemplate = Template(filename='template_data.txt')
print(mytemplate.render())

# Creates directory ./mako_module and converts ./template_data.txt to a Python source file
mytemplate2 = Template(filename='template_data.txt', module_directory='mako_module')
print(mytemplate2.render())

# Output

# # -*- coding:ascii -*-
# from mako import runtime, filters, cache
# UNDEFINED = runtime.UNDEFINED
# STOP_RENDERING = runtime.STOP_RENDERING
# __M_dict_builtin = dict
# __M_locals_builtin = locals
# _magic_number = 10
# _modified_time = 1458857247.295091
# _enable_loop = True
# _template_filename = 'template_data.txt'
# _template_uri = 'template_data.txt'
# _source_encoding = 'ascii'
# _exports = []
#
#
# import os
#
# def render_body(context,**pageargs):
#     __M_caller = context.caller_stack._push_frame()
#     try:
#         __M_locals = __M_dict_builtin(pageargs=pageargs)
#         __M_writer = context.writer()
#         __M_writer(u'\n\nMy python code can be imported here!!!\n')
#         return ''
#     finally:
#         context.caller_stack._pop_frame()
#
#
# """
# __M_BEGIN_METADATA
# {"source_encoding": "ascii", "line_map": {"16": 1, "18": 0, "29": 23, "23": 1}, "uri": "template_data.txt", "filename": "template_data.txt"}
# __M_END_METADATA
# """