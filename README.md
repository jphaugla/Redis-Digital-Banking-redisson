# Redisearch-Digital-Banking-redisson

Provides a quick-start example of using Redis with springBoot and redisson with Banking structures.  Digital Banking uses an API microservices approach to enable high speed requests for account, customer and transaction information.  As seen below, this data is useful for a variety of business purposes in the bank.
<a href="" rel="Digital Banking"><img src="images/DigitalBanking.png" alt="" /></a>

### Note:  This is the same as Redisearch-Digital-Banking but uses redisson redistemplate instead of any of the crudrepository indexes.  redisearch 2.0 indexes will be used.  This is not using the crudrepository for the basic redis data. 

## Overview
In this tutorial, a java spring boot application is run through a jar file to support typical API calls to a REDIS banking data layer.  A redis docker configuration is included.
Additionally, a version of this with SSL is documented with optional redisson yaml containing ttl

## Redis and Redisson advantages for Digital Banking
 * Redis easily handles high write transaction volume
 * Redis has no tombstone issues and can upsert posted transactions over pending
 * Redis Enterprise scales vertically (large nodes)  and horizontally (many nodes)
 * Redisearch 2.0 automatically indexes the hash structure created by Spring Java CRUD repository
 * Redisson will use read replicas to distribute the read workload across a primary and multiple replicas

## Deployment options 
### Run on local machine with docker
* Docker installed on your local system, see [Docker Installation Instructions](https://docs.docker.com/engine/installation/).
* Can run docker using application in docker hub (commented out in docker-compose.yaml)
* When using Docker for Mac or Docker for Windows, the default resources allocated to the linux VM running docker are 2GB RAM and 2 CPU's. Make sure to adjust these resources to meet the resource requirements for the containers you will be running. More information can be found here on adjusting the resources allocated to docker.
[Docker for mac](https://docs.docker.com/docker-for-mac/#advanced)
[Docker for windows](https://docs.docker.com/docker-for-windows/#advanced)
### Run on local machine without docker
* Still run the redis on docker but run the application in intelli4j or from jar command
* Must set environment variables
### Run docker hub container and redis enterprise on kubernetes
* Yaml files provided

## Links that help!

 * [spring data for redis github](https://github.com/spring-projects/spring-data-examples/tree/master/redis/repositories)
 * [spring data for redis sample code](https://www.oodlestechnologies.com/blogs/Using-Redis-with-CrudRepository-in-Spring-Boot/)
 * [lettuce tips redis spring boot](https://www.bytepitch.com/blog/redis-integration-spring-boot/)
 * [spring data Reference in domain](https://github.com/spring-projects/spring-data-examples/blob/master/redis/repositories/src/main/java/example/springdata/redis/repositories/Person.java)
 * [spring data reference test code](https://github.com/spring-projects/spring-data-examples/blob/master/redis/repositories/src/test/java/example/springdata/redis/repositories/PersonRepositoryTests.java)
 * [spring async tips](https://dzone.com/articles/effective-advice-on-spring-async-part-1)
 * [brewdis sample application](https://github.com/redis-developer/brewdis)
 * [redis-developer lettucemod mesclun](https://github.com/redis-developer/lettucemod)
 * [redisson with spring boot starter](https://github.com/redisson/redisson/tree/master/redisson-spring-boot-starter#2-add-settings-into-applicationsettings-file)
 * [redisson with primary and replica shards](https://redisson.org/glossary/redis-master-slave-replication.html)
 * [Setting up SSL with Redis Enterprise](https://tgrall.github.io/blog/2020/01/02/how-to-use-ssl-slash-tls-with-redis-enterprise/)
 * [Kubernetes Install Redis Enterprise](https://github.com/RedisLabs/redis-enterprise-k8s-docs#installation)

## Technical Overview

This github java code uses the redisson java library with spring boot starter for redis.  

### The spring java code
This is basic spring links
* [Spring Redis](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis.repositories.indexes)
* *config*-Initial configuration module using autoconfiguration and a threadpool sizing to adjust based on machine size
* *controller*-http API call interfaces
* *data*-code to generate POC type of customer, account, and transaction code
* *domain*-has each of the java objects with their columns.  Enables all the getter/setter methods
* *repository*-This is repository layer
* *service*-asyncservice and bankservice doing the interaction with redis
### 
The java code demonstrates common API actions with the data layer in REDIS.  The java spring Boot framework minimizes the amount of code to build and maintain this solution.  Maven is used to build the java code and the code is deployed to the tomcat server.

### Data Structures in use
<a href="" rel="Tables Structures Used"><img src="images/Tables.png" alt="" /></a>

## Getting Started using Docker desktop
* Prepare Docker environment-see the Prerequisites section above...
* Pull this github into a directory
```bash
git clone https://github.com/jphaugla/Redisearch-Digital-Banking.git
```
* Refer to the notes for redis Docker images used but don't get too bogged down as docker compose handles everything except for a few admin steps on tomcat.
 * [https://hub.docker.com/r/bitnami/redis/](https://hub.docker.com/r/bitnami/redis/)  
* Open terminal and change to the github home where you will see the docker-compose.yml file, then: 
```bash
docker-compose up -d
```
this environment has 4 docker containers, 3 redis containers (redis, redis2, redis3).  redis2 and redis3 are replicas of the primary redis instance in the container simply named "redis".
NOTE:  redisson splits read load evenly across the three redis containers
This docker compose will build the docker image for the java application using "docker compose build" and deploy it to docker.  

## Run java application locally against redis in containers
The following steps are if you want to run on a separate environment without docker.  To do this, comment out the bankapp from the docker image
* turn off the bankapp docker container to run locally instead
```bash
docker stop bankapp
```

* Install maven and java
```bash
sudo apt-get install maven
sudo apt-get install default-jdk
```
* Pull this github into a directory
```bash
git clone https://github.com/jphaugla/Redisearch-Digital-Banking.git
```

## Execute sample application 

* Compile the code
```bash
mvn package
```
* run the jar file after setting up required environment variables. 
  * Also have set up a script to do these environment variables instead of typing this under ./scripts/setEnv.sh to use this
```bash
source ./scripts/setEnv.sh
```
* alternatively, these are the commands

One "learning" on the redisson yaml:  the password must be the same on all the redis databases whether they are primary or secondary.
There is only one password parameter and you cannot use the ":password" embedded in the URL to to add the password
```bash
export REDIS_CONNECTION="redis://127.0.0.1:6379"
export REDIS_REPLICA1="redis://127.0.0.1:6380"
export REDIS_REPLICA2="redis://127.0.0.1:6381"
# this is the number of threads used to generate data
export CORE_POOLSIZE=23
# this spreads read load across primary and replica shards
#  setting this to "SLAVE" will only use the 2 replica shards for the reads
export READ_MODE=MASTER_SLAVE
export REDIS_PASSWORD=somesillypw
# this is only needed for running raw redisson with scripts/generateRedisson.sh
export REDISSON_YAML_PATH=src/main/resources/redisson-replica.yaml
java -jar target/redis-0.0.1-SNAPSHOT.jar
```
*  Test the application from a separate terminal window.  This script uses an API call to generate sample banking customers, accounts and transactions.  It uses Spring ASYNC techniques to generate higher load.  A flag chooses between running the transactions pipelined in Redis or in normal non-pipelined method.
```bash
./scripts/generateData.sh
```
Shows a benchmark test run of  generateData.sh on GCP servers.  Although, this test run is using redisearch 1.0 code base.  Need to rerun this test.
<a href="" rel="Generate Data Benchmark"><img src="images/Benchmark.png" alt="" /></a>

## Deploy on Redis on Kubernetes
* Follow [Redis Enterprise k8s installation instructions](https://github.com/RedisLabs/redis-enterprise-k8s-docs#installation) all the way through-no need in demo to do the more complex webhook parts of step 5.  A yaml file for step 6 is created so don't perform step 6 in the instructions.
* Instead of doing the above step 5, use the file in the repository
```bash
# create primary database
cd k8s
kubectl apply -f redis-enterprise-database.yml
```
* Since the other two databases need to be replicas of the primary database, get the secret for the primary database URI
  * use port forwarding to access the redis cluster 
```bash
kubectl port-forward service/rec-ui 8443:8443
```
* from chrome or firefox browser, [https://localhost:8443](https://localhost:8443)
* to log in, the cluster username and password will be needed
```bash
./getClusterUnPw.sh
```
* log in with retrieved username and password
* Click on databases tab
* click on the newly created database
* Click on the *Get Replica of source URL" and copy it to the clipboard
* <a href="" rel="Generate Data Benchmark"><img src="images/getreplicaURL.png" alt="" /></a>
* this URL needs to be turned into a secret using base64.  Substitute the captured uri for this uri
```bash
echo redis://admin:Cl5rhnQcAXTK21JKxxxxxxxxxFnLPjKIZy89CsqAABTKOK3v@redis-14376.rec.demo.svc.cluster.local:14376 |base64
```
* paste this secret into the urlSecret.yml file replacing the existing secret
* create this secret
```bash
kubectl apply urlSecret.yml
```
* the other two database files can now be applied.
  * NOTE:  the database secret name for redis2 and redis3 are changed to be the same secret that was automatically created for the primary database.  This was done because redisson only has one parameter for database password so all have to be the same
  * redis2 and redis3 also use the secret created above for the primary database uri
```bash
kubectl apply -f redis2.yml
kubectl apply -f redis3.yml
```

## Deploy bankapp on Kubernetes
* modify, create the environmental variables by editing configmap.yml
  * can find the IP addresses and ports for each of the databases by running ```kubectl get services```
  * put the database password in for the redis password by running ```getDatabasePw```
* create the configuration map
```bash
kubectl apply -f configmap.yaml 
```
* deploy the bankapp
```bash
kubectl apply -f bankapp.yml
```
* port forward and continue with testing of the APIs
```bash
kubectl port-forward service/redis-bankapp-service 8080:8080
```
* can also use port forwarding to redis-cli to each of the 3 databases

##  Investigate the APIs 
These scripts are in ./scripts
  * addTag.sh (currently not implemented) - add a tag to a transaction.  Tags allow user to mark  transactions to be in a buckets such as Travel or Food for budgetary tracking purposes
  * deleteCustomer.sh (currently not implemented) - delete all customers matching a string
  * generateData.sh - simple API to generate default customer, accounts, merchants, phone numbers, emails and transactions
  * generateLots.sh - for server testing to generate higher load levels.  Use with startAppservers.sh.  Not for use with docker setup.  This is load testing with redis enterprise and client application running in same network in the cloud.
  * generateRedisson.sh - writes a few commands using a base redisson connection outside of redistemplate
  * getByAccount.sh (currently not implemented) - find transactions for an account between a date range
  * getByCreditCard.sh (currently not implemented) - find transactions for a credit card  between a date range
  * getCustomerByEmails.sh - gets email for customer ids starting from 100001 for the desired range - this is used for generating read load
  * getCustomerByPhone.sh - gets phone numbers for customer ids starting from 100001 for the desired range - this is used for generating read load
  * getCustomers.sh - gets a range of customer id starting from 100001 for the desired range - this is used for generating read load
  * getCustomerEmails.sh - gets email for customer ids starting from 100001 for the desired customer range - this is used for generating read load
  * getCustomerPhones.sh - gets phone numbers  for customer ids starting from 100001 for the desired range - this is used for generating read load
  * getByCustID.sh - retrieve transactions for customer
  * getByEmail.sh - retrieve customer record using email address
  * getByMerchant.sh (currently not implemented) - find all transactions for an account from one merchant for date range
  * getByMerchantCategory.sh (currently not implemented) - find all transactions for an account from merchant category for date range
  * getByNamePhone.sh (currently not implemented) - get customers by phone and full name.
  * getByPhone.sh - get customers by phone only
  * getByStateCity.sh (currently not implemented)  - get customers by city and state
  * getByZipLastname.sh - (currently not implemented) get customers by zipcode and lastname.
  * getReturns.sh (currently not implemented)  - get returned transactions count by reason code
  * getTags.sh (currently not implemented) - get all tags on an account
  * getTaggedAccountTransactions.sh (currently not implemented) - find transactions for an account with a particular tag
  * getTransaction.sh - get one transaction by its transaction ID
  * getTransactionStatus.sh (currently not implemented) - see count of transactions by account status of PENDING, AUTHORIZED, SETTLED
  * loop.sh - used to load testing to keep infinite loop running.  pass parameter of script to run
  * putCustomer.sh - put a set of json customer records
  * saveAccount.sh - save a sample account
  * saveCustomer.sh - save a sample customer
  * saveTransaction.sh - save a sample Transaction
  * startAppservers.sh - start multiple app server instances for load testing
  * testPipeline.sh - test pipelining
  * updateTransactionStatus.sh (currently not implemented) - generate new transactions to move all transactions from one transaction Status up to the next transaction status. Parameter is target status.  Can choose SETTLED or POSTED.  Will move 100,000 transactions per call

## Using repository to generate load
* to generate data, use generateData.sh
* to generate load, use the loop.sh script to create an infinite loop.  Pass a parameter of the script to use for the load
```bash
./generateData.sh
./loop.sh getCustomers.sh
```
```bash
./loops.sh getCustomerEmails.sh
```

## Using redisson with Redis Enterprise and TLS

[This blog helps with TLS configuration with Redis Enterprise](https://tgrall.github.io/blog/2020/01/02/how-to-use-ssl-slash-tls-with-redis-enterprise/)
Additional note, instead of using stunnel for testing redis-cli, see command after environment is established


* change environment variable to use redisson yaml file with SSL and have extra "s" on redis URI
```bash
export KEYSTORE_PASSWORD=sillyPassword
export TRUSTSTORE_PASSWORD=sillyPassword
export REDIS_CONNECTION="rediss://localhost:6379"
export REDISSON_YAML_PATH=src/main/resources/redisson-ssl.yaml
```
* generate required keys
  *  copy in proxy certificate into same ssl folder and name it proxy_cert.pem
```bash
cd src/main/resources/ssl
./generatepems.sh
# must type in passwords matching the environment variables when prompted below
./generatekeystore.sh
./generatetrust.sh
./importkey.sh
```
```bash
redis-cli -u $REDIS_CONNECTION --tls --cacert src/main/resources/ssl/proxy_cert.pem --cert src/main/resources/ssl/client_cert_app_001.pem --key  src/main/resources/ssl/client_key_app_001.pem -a $REDIS_PASSWORD
```
* package and run application
```bash
mvn clean package
java -jar  target/redis-0.0.1-SNAPSHOT.jar
```
