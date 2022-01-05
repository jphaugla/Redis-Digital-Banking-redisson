# gets a range of customer id starting from 100001 for the desired range
# this is used for generating read load
curl -X GET -H "Content-Type: application/json"  'http://localhost:8080/customerLoop/?numberRange=20'
