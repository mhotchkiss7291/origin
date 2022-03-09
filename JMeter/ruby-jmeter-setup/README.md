**Steps to run a JMeter test of Gaia Server**

Assuming that you have installed Ruby properly:

    cd load_test

    # should say 2.5.0, because of .ruby-version (rvm) or .tool-versions (asdf-vm)
    ruby -v

    # installs ruby-jmeter
    bundle

1. Set TEST_ENV for the selected environment (Not Prod)
2. Modify Ruby code for functionality in siren_server_test.rb and then for thread settings 
3. Clean the leftover file run:

	clean.sh

4. Run the command to generate the ruby-jmeter.jmx file and place it in the 
docker-jmeter/siren-server/ directory

	build_jmeter.sh

This will run the tests and generate the siren-server.jtl with the test result data
and generate the following files and directories.

- **tests/siren-server/siren-server.jtl** (CSV file for reports)
- **tests/siren-server/report/README.TXT** (Readable report with data, but not the best)
- **tests/siren-server/report/content** (Report stylesheets)
- **tests/siren-server/report/sbadmin2-1.0.7** (Presumably HTML presentation library?)
- **tests/siren-server/report/index.html** (The report's main page)

5. Open tests/siren-server/report/index.html and browse the results

    load_test master % THREAD_COUNT=100 ./run_with_defaults.sh
