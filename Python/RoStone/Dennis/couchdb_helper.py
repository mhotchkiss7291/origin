import pycouchdb
from app_helper import config

HOST = config.couchdb_host
USERNAME = config.couchdb_username
PASSWORD = config.couchdb_password
SIREN_DATABASE = config.couchdb_siren_database
SCHOLAR_DATABASE = config.couchdb_scholar_database
TUTORING_SESSIONS_DATABASE = config.couchdb_tutoring_sessions_database

def couchdb_connect(database, host=HOST, username=USERNAME, password=PASSWORD):
    params_dict = locals()
    server = pycouchdb.Server(('http://{username}:{password}@{host}').format(**params_dict))
    return server.database(database)
    
    
def get_doc(database, document):
    try:
        if isinstance(document, dict): document_id = document['_id']
        else: document_id = document
        return database.get(document_id)
    except Exception, error: return str(error)
    
    
def post_doc(database, document):
    try: return database.save(document)
    except Exception, error: return str(error)
    

def put_doc(database, document):
    try: 
        current_document = get_doc(database, document)
        updated_document = dict(current_document.items() + document.items())
        return database.save(updated_document)
    except Exception, error: return str(error)
    

def delete_doc(database, document):
    try: return database.delete(get_doc(database, document))
    except Exception, error: return str(error)
    

# NOTE: the helper functions below are specific to Siren documents!
  
def get_siren_doc(siren_document):
    database = couchdb_connect(SIREN_DATABASE)
    return get_doc(database, siren_document)
    
    
# NOTE: the helper functions below are specific to Scholar documents!
  
def get_scholar_doc(scholar_document):
    database = couchdb_connect(SCHOLAR_DATABASE)
    return get_doc(database, scholar_document)
    
    
# NOTE: the helper functions below are specific to Tutoring Sessions documents!
  
def get_tutoring_session_doc(tutoring_session_document):
    database = couchdb_connect(TUTORING_SESSIONS_DATABASE)
    return get_doc(database, tutoring_session_document)
    
