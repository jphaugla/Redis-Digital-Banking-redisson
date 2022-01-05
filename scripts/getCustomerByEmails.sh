# Use emal to look up a customer.  Do this for a range of emails
# this is used for load testing
curl -X GET -H "Content-Type: application/json"  'http://localhost:8080/emailLoop/?numberRange=200'
