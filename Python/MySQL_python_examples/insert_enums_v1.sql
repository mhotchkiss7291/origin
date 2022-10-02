USE soundlogs;

# voice_type
INSERT INTO voice_types (voice_type) VALUES ('female');
INSERT INTO voice_types (voice_type) VALUES ('male');
INSERT INTO voice_types (voice_type) VALUES ('child'); 

# language_codes
INSERT INTO language_codes (language_code) VALUES ('en-US');
INSERT INTO language_codes (language_code) VALUES ('es-419');
INSERT INTO language_codes (language_code) VALUES ('de-DE');

# session_types
INSERT INTO session_types (session_type) VALUES ('ListenForPhrasesGMMSession');
INSERT INTO session_types (session_type) VALUES ('ListenForPhrasesDNNSession');
