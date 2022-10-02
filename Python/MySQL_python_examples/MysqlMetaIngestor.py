#! /usr/bin/env python
import logging
import csv
import os
import argparse
import datetime 
from configparser import ConfigParser
import pymysql

class MysqlMetadataIngestor:

    def __init__(self, source_dir):
        self.source_dir = source_dir
        self.conn = None

    def goObject(self):
        setupDatabase(self)
        resetTables(self)
        insertData(self)
        teardownDatabase(self)

def setupDatabase(self):
    if not self.conn:
        params = config()
        self.conn = pymysql.connect(**params) 
        '''
        cur = self.conn.cursor()
        cur.execute("select @@version")
        output = cur.fetchall()
        print(output)
        self.conn.close()
        '''

def teardownDatabase(self):
    if self.conn:
        self.conn.close()

def config(filename='database.ini', section='mysql'):
    parser = ConfigParser()
    parser.read(filename)
    db = {}
    if parser.has_section(section):
        params = parser.items(section)
        for param in params:
            db[param[0]] = param[1]
    else:
        raise Exception('Section {0} not found in the {1} file'.format(section, filename))
    return db

def resetTables(self):
    os.system('mysql -u mhotchkiss -ppassword < drop_tables_mysql_v1.sql')
    os.system('mysql -u mhotchkiss -ppassword < create_tables_mysql_v1.sql')
    os.system('mysql -u mhotchkiss -ppassword < insert_enums_v1.sql')

def selectAuthorAssociationId(self, language_code_id, author, voice_type_id):
    cur = self.conn.cursor()
    sql = ("""
        SELECT author_association_id
        FROM   author_associations
        WHERE  language_code_id = %s
            AND author = %s
            AND voice_type_id = %s 
        """)
    params = (language_code_id, author, voice_type_id)
    cur.execute(sql, params)
    rows = cur.fetchall()
    author_association_id = 0
    if len(rows) != 0:
        for r in rows:
            author_association_id = r[0]
    cur.close()
    return author_association_id

def insertAuthorAssociationId(self, language_code_id, author, voice_type_id):
    insert = (
        """
        INSERT INTO author_associations
            (language_code_id,
             author,
             voice_type_id)
        VALUES      (%s, %s, %s) 
        """)
    params = (language_code_id, author, voice_type_id)
    cur = self.conn.cursor()
    cur.execute(insert, params)
    cur.execute('SELECT LAST_INSERT_ID()')
    author_association_id = (cur.fetchone()[0])
    self.conn.commit()
    return author_association_id

def selectPromptId(self, language_code_id, expected_response):
    query = (
        """
        SELECT prompt_id
        FROM   prompts
        WHERE  language_code_id = %s
            AND expected_response = %s
        """)
    params = (language_code_id, expected_response)
    cur = self.conn.cursor()
    cur.execute(query, params)
    rows = cur.fetchall()
    prompt_id = 0
    if len(rows) != 0:
        for r in rows:
            prompt_id = r[0]
    return prompt_id

def insertPromptId(self, language_code_id, expected_response):
    insert = (
        """
        INSERT INTO prompts
            (language_code_id,
             expected_response)
        VALUES      (%s, %s) 
        """)
    params = (language_code_id, expected_response)
    cur = self.conn.cursor()
    cur.execute(insert, params)
    cur.execute('SELECT LAST_INSERT_ID()')
    prompt_id = (cur.fetchone()[0])
    self.conn.commit()
    return prompt_id

def insertSoundlog(self,
    soundlog_id, 
    language_code_id,
    author_association_id,
    creation_date, 
    audio_size, 
    session_type_id, 
    prompt_id, 
    overall_score, 
    phoneme_response, 
    xml_path, 
    audio_path
    ):
    insert = (
        """
        INSERT INTO soundlogs
            (soundlog_id, 
            language_code_id,
            author_association_id,
            creation_date,
            audio_size,
            session_type_id,
            prompt_id,
            overall_score,
            phoneme_response,
            xml_path,
            audio_path)
        VALUES  (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """)
    params = (
        soundlog_id, 
        language_code_id, 
        author_association_id, 
        creation_date, 
        audio_size, 
        session_type_id, 
        prompt_id, 
        overall_score, 
        phoneme_response, 
        xml_path, 
        audio_path
    )
    cur = self.conn.cursor()
    cur.execute(insert, params)
    self.conn.commit()

def loadEnumValues(self):
    self.voice_types_dict = {}
    sql = "SELECT voice_type_id, voice_type FROM voice_types"  
    cur = self.conn.cursor()
    cur.execute(sql)
    for (voice_type_id, voice_type) in cur:
        self.voice_types_dict[voice_type] = voice_type_id
    self.language_codes_dict = {}
    sql = "SELECT language_code_id, language_code FROM language_codes"  
    cur.execute(sql)
    for (language_code_id, language_code) in cur:
        self.language_codes_dict[language_code] = language_code_id
    self.session_types_dict = {}
    sql = "SELECT session_type_id, session_type FROM session_types"  
    cur.execute(sql)
    for (session_type_id, session_type) in cur:
        self.session_types_dict[session_type] = session_type_id

def formatTimestamp(creation_date):
    creation_date_formatted = datetime.datetime.strptime(creation_date,'%Y-%m-%dT%H:%M:%SZ').strftime('%Y-%m-%d %H:%M:%S')
    return creation_date_formatted

def insertData(self):

    loadEnumValues(self)

    with open('metaData.csv', 'r') as read_obj:
        csv_reader = csv.reader(read_obj)
        meta_rows = list(csv_reader)
        #print(meta_rows)

    # Skip header row
    i = 1
    while i < len(meta_rows):

        # author_associations
        author = meta_rows[i][1]
        language_code = meta_rows[i][2]
        language_code_id = self.language_codes_dict.get(language_code)
        voice_type = meta_rows[i][3]
        voice_type_id = self.voice_types_dict.get(voice_type)
        author_association_id = selectAuthorAssociationId(self, language_code_id, author, voice_type_id)
        if author_association_id == 0:
            author_association_id = insertAuthorAssociationId(self, language_code_id, author, voice_type_id)
        
        # prompts
        expected_response = meta_rows[i][7]
        prompt_id = selectPromptId(self, language_code_id, expected_response)
        if prompt_id == 0:
            prompt_id = insertPromptId(self, language_code_id, expected_response)
        #print('prompt_id = ' + str(prompt_id))

        # soundlogs
        soundlog_id = meta_rows[i][0]
        creation_date = meta_rows[i][4]
        creation_date_formatted = formatTimestamp(creation_date)
        audio_size = meta_rows[i][5]
        session_type = meta_rows[i][6]
        session_type_id = self.session_types_dict.get(session_type)
        overall_score = meta_rows[i][8]
        phoneme_response = meta_rows[i][9]
        xml_path = meta_rows[i][10]
        audio_path = meta_rows[i][11]
        insertSoundlog(
            self,
            soundlog_id,
            language_code_id,
            author_association_id,
            creation_date_formatted,
            audio_size,
            session_type_id,
            prompt_id,
            overall_score,
            phoneme_response,
            xml_path,
            audio_path
        )
        i += 1

def main():
    logging.basicConfig(level=logging.INFO)
    #parser = argparse.ArgumentParser(description='blank')
    #parser.add_argument('-d', '--directory', required=1)
    #args = parser.parse_args()
    #connection = Connector(args.directory)
    connection = MysqlMetadataIngestor('.')
    connection.goObject()

main()
