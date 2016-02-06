rm -rf ./asgard
git clone https://mhotchkiss@stash.trstone.com/scm/automation/asgard.git
echo "Git Done.."
cp app.cfg ./asgard/util/src/python/config
echo "app.cfg copied"
pip install -r ./asgard/util/src/python/config/requirements.txt
echo "requirements.txt installed"
cd ./asgard/util/src/python
echo "Move to test dir, running tests.."
# All output to file
nosetests -v ./test/biblio &> ~/TestReport.out
