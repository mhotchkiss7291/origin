curl \
'https://ego-qa3.dev.rosettastone.com/api/users' \
-H 'Authorization: Bearer bf500ee6-4732-451e-9e61-0f245b6d6050' \
-H 'Origin: https://admin-qa3.dev.rosettastone.com' \
-H 'Content-Type: application/json;charset=UTF-8' \
-H 'Accept: application/json' \
--data-binary ' \
{
  "roles": [
    
  ],
  "targetLanguage": {
    "language": "en-US"
  },
  "organization": "3e8a22e4-2300-40a7-8ac9-c5d96c5d9082",
  "firstName": "Clean",
  "lastName": "Will",
  "email": "cw@flla.test",
  "interfaceLanguage": "en-US"
}
' \
--compressed
