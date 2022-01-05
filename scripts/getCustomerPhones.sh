# gets phone numbers  for customer ids starting from 100001 for the desired range
# this is used for generating read load
curl -X GET -H "Content-Type: application/json"  'http://localhost:8080/customerPhoneLoop/?numberRange=200'
