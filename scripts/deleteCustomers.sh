# retrieve set of customer IDS using redisearch
# delete set of Ids
curl -X GET -H "Content-Type: application/json"  'http://localhost:8080/deleteCustomer/?customerString=cust0*'
