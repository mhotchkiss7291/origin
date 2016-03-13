#Tests the endpoint /content/user_activity_set_summary/<userId> returns ... FAIL
#Tests the endpoint /content/user_activity_set_summary/<userId>?course=<courseId> returns ... FAIL
nosetests -v -a 'acceptance','mrh' --with-xunit test/biblio/content_test.py:mrh_stuff
