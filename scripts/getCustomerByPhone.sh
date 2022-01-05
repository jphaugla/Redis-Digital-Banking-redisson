# Use phone number to look up a customer.  Do this for a range of phone numbers
# this is used for load testing
curl -X GET -H "Content-Type: application/json"  'http://localhost:8080/phoneLoop/?numberRange=200'
