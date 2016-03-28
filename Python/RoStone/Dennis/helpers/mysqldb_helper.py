import dataset
import random
from app_helper import config

def mysqldb_connect(host, username, password, database):
    params_dict = locals()
    if password: url = 'mysql://{username}:{password}@{host}/{database}'.format(**params_dict)
    else: url = 'mysql://{username}@{host}/{database}'.format(**params_dict)
    return dataset.connect(url)
    

def select_record(database, table, query_dict):
    try: return dict(database[table].find_one(**query_dict))
    except Exception, error: return str(error)

    
def insert_record(database, table, data_dict):
    try:
        database[table].insert(data_dict)
        return select_record(database, table, data_dict)
    except Exception, error: return str(error)

    
def update_record(database, table, query_dict, data_dict):
    try:
        record = select_record(database, table, query_dict)
        database[table].update(dict(record.items() + data_dict.items()), query_dict.keys())
        return select_record(database, table, query_dict)
    except Exception, error: return str(error)
    

def delete_record(database, table, query_dict):
    try:
        database[table].delete(**query_dict)
        return select_record(database, table, query_dict)
    except Exception, error: return str(error)
    

def mysqldb_ego_connect():
    ego_host = random.choice(config.mysqldb_ego_hosts)
    ego_username = config.mysqldb_ego_username
    ego_password = config.mysqldb_ego_password
    ego_database = config.mysqldb_ego_database
    return mysqldb_connect(ego_host, ego_username, ego_password, ego_database)
    
    
def select_ego_record(table, query_dict):
    database = mysqldb_ego_connect()
    return select_record(database, table, query_dict)


def insert_ego_record(table, data_dict):
    database = mysqldb_ego_connect()
    return insert_record(database, table, data_dict)


def update_ego_record(table, query_dict, data_dict):
    database = mysqldb_ego_connect()
    return update_record(database, table, query_dict, data_dict)


def delete_ego_record(table, query_dict):
    database = mysqldb_ego_connect()
    return delete_record(database, table, query_dict)