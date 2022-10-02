USE soundlogs;

CREATE TABLE voice_types
  (
     voice_type_id INTEGER NOT NULL auto_increment,
     voice_type    ENUM('female', 'male', 'child') NOT NULL,
     PRIMARY KEY (voice_type_id),
     CONSTRAINT voice_types_u1 UNIQUE(voice_type)
  );

CREATE TABLE language_codes
  (
     language_code_id INTEGER NOT NULL auto_increment,
     language_code    ENUM('en-US', 'es-419', 'de-DE') NOT NULL,
     PRIMARY KEY (language_code_id),
     CONSTRAINT language_codes_u1 UNIQUE(language_code)
  );

CREATE TABLE session_types
  (
     session_type_id INTEGER NOT NULL auto_increment,
     session_type    ENUM('ListenForPhrasesGMMSession',
                           'ListenForPhrasesDNNSession') NOT NULL,
     PRIMARY KEY (session_type_id),
     CONSTRAINT session_types_u1 UNIQUE(session_type)
  );

CREATE TABLE author_associations
  (
     author_association_id BIGINT NOT NULL auto_increment,
     language_code_id      INTEGER NOT NULL,
     author                VARCHAR(36) NOT NULL,
     voice_type_id         INTEGER NOT NULL,
     PRIMARY KEY (author_association_id),
     FOREIGN KEY (language_code_id) REFERENCES language_codes (language_code_id),
     FOREIGN KEY (voice_type_id) REFERENCES voice_types (voice_type_id),
     CONSTRAINT author_associations_u1 UNIQUE(author, language_code_id, voice_type_id),
     CONSTRAINT author_associations_u2 UNIQUE(language_code_id, author, voice_type_id)
  );

CREATE TABLE prompts
  (
     prompt_id         BIGINT NOT NULL auto_increment,
     language_code_id  INTEGER NOT NULL,
     expected_response VARCHAR(255) NOT NULL,
     PRIMARY KEY (prompt_id),
     CONSTRAINT prompts_u1 UNIQUE(expected_response, language_code_id),
     CONSTRAINT prompts_u2 UNIQUE(language_code_id, expected_response),
     FOREIGN KEY (language_code_id) REFERENCES language_codes (language_code_id)
  );

CREATE TABLE soundlogs
  (
     soundlog_id           VARCHAR(36) NOT NULL,
     language_code_id      INTEGER NOT NULL,
     author_association_id BIGINT NOT NULL,
     creation_date         TIMESTAMP,
     audio_size            INTEGER NOT NULL,
     session_type_id       INTEGER NOT NULL,
     prompt_id             BIGINT NOT NULL,
     overall_score         INTEGER NOT NULL,
     phoneme_response      VARCHAR(255),
     xml_path              VARCHAR(255) NOT NULL,
     audio_path            VARCHAR(255) NOT NULL,
     PRIMARY KEY (soundlog_id),
     FOREIGN KEY (author_association_id) REFERENCES author_associations (author_association_id), 
     FOREIGN KEY (prompt_id) REFERENCES prompts (prompt_id), 
     FOREIGN KEY (language_code_id) REFERENCES language_codes (language_code_id),
     FOREIGN KEY (session_type_id) REFERENCES session_types (session_type_id)
  ); 
