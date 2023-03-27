# Redisearch-Digital-Banking-redisson

Provides a quick-start example of using Redisson with springBoot and redisson with Banking structures.  Additionally, there is a directory called *simple* demonstrating redission pro with client side caching.  Digital Banking uses an API microservices approach to enable high speed requests for account, customer and transaction information.  As seen below, this data is useful for a variety of business purposes in the bank.
<a href="" rel="Digital Banking"><img src="images/DigitalBanking.png" alt="" /></a>

- [Overview](#overview)
- [Important Links](#important-linksnotes)
- [Technical Overview](#technical-overview)
  - [Spring Java Code](#the-spring-java-code)
  - [Data Structures](#data-structures)
- [Instructions](#instructions)
  - [Create Environment](#create-environment)
  - [Multiple Deployment options](#multiple-options-for-creating-the-environment)
  - [Docker Compose](#docker-compose)
  - [Deploy on Linux](#deploy-linux)
    - [Compile the Application](#compile-the-application)
    - [Set Environment](#set-environment)
  - [Redisson yaml configuration](#redisson-yaml-files)
  - [Kubernetes](#kubernetes)
    - [Install Redis Enterprise](#install-redis-enterprise)
    - [Add Redisinsights](#add-redisinsights)
    - [Deploy application](#deploy-bankapp-on-kubernetes)
  - [Use the Application](#use-the-application)
    - [Investigate the APIs](#investigate-the-apis)
    - [Generate Load](#generate-load)
    - [Redisson with TLS](#redisson-with-tls)
  - [Redisson Client-side-caching](#redisson-client-side-caching)
    - [Redisson Pro or open source](#redisson-pro-or-open-source)
    - [Client-side-caching instructions](#redisson-client-side-caching-instructions)
      - [Set the environment](#set-client-side-caching-environment)
      - [Edit for tests to run](#edit-test-programs-to-run)
      - [Compile Client Side Caching](#compile-client-side-caching)
      - [Execute the jar file](#run-the-sample-program)


### Note:  These other githubs are similar:  
* Redisearch-Digital-Banking but uses redisson redistemplate instead of the crudrepository indexes.  redisearch 2.0 indexes will be used.  This is not using the crudrepository for the basic redis data. 

## Overview
In this tutorial, a java spring boot application is run through a jar file to support typical API calls to a REDIS banking data layer.  A redis docker configuration is included.
Additionally, a version of this with SSL is documented with optional redisson yaml containing ttl.  In a subdirectory, a redisson with client side caching is demostrated.

## Redisson and Redis Advantages for Digital Banking
 * Redis easily handles high write transaction volume
 * Redis has no tombstone issues and can upsert posted transactions over pending
 * Redis Enterprise scales vertically (large nodes)  and horizontally (many nodes)
 * Redisson will use read replicas to distribute the read workload across a primary and multiple replicas
 * Redisson has client side caching capability

## Important Links/Notes
### Spring Data
* [spring data for redis github](https://github.com/spring-projects/spring-data-examples/tree/master/redis/repositories)
* [spring data for redis sample code](https://www.oodlestechnologies.com/blogs/Using-Redis-with-CrudRepository-in-Spring-Boot/)
* [lettuce tips redis spring boot](https://www.bytepitch.com/blog/redis-integration-spring-boot/)
* [spring data Reference in domain](https://github.com/spring-projects/spring-data-examples/blob/master/redis/repositories/src/main/java/example/springdata/redis/repositories/Person.java)
* [spring data reference test code](https://github.com/spring-projects/spring-data-examples/blob/master/redis/repositories/src/test/java/example/springdata/redis/repositories/PersonRepositoryTests.java)
* [spring async tips](https://dzone.com/articles/effective-advice-on-spring-async-part-1)
* [redis-developer lettucemod](https://github.com/redis-developer/lettucemod)
### Redisson
* [redisson with spring boot starter](https://github.com/redisson/redisson/tree/master/redisson-spring-boot-starter#2-add-settings-into-applicationsettings-file)
* [redisson with primary and replica shards](https://redisson.org/glossary/redis-master-slave-replication.html)
* [Setting up SSL with Redis Enterprise](https://tgrall.github.io/blog/2020/01/02/how-to-use-ssl-slash-tls-with-redis-enterprise/)
* [Kubernetes Install Redis Enterprise](https://github.com/RedisLabs/redis-enterprise-k8s-docs#installation)
* [Redisson and local cache intro](https://redisson.org/glossary/redis-caching.html)
* [Redisson open source and pro features](https://redisson.pro/#compare-editions)
* [Redisson near cache](https://dzone.com/articles/how-to-boost-redis-with-local-caching-in-java)
* [Redisson from Baeldung](https://www.baeldung.com/redis-redisson)
* [Redisson Distributed Collections](https://github.com/redisson/redisson/wiki/7.-Distributed-collections)
* [Redisson configuration](https://github.com/redisson/redisson/wiki/2.-Configuration)
### Related githubs
* [brewdis sample application](https://github.com/redis-developer/brewdis)
* [Digital Banking github using CRUD repository](https://github.com/jphaugla/Redis-Digital-Banking)
* [Digital Banking with redistemplate and redisearch](https://github.com/jphaugla/Redisearch-Digital-Banking-redisTemplate)

## Technical Overview
### The spring java code
This is basic spring links
* [Spring Redis](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis.repositories.indexes)
* *config*-Initial configuration module with redisson connection factory using autoconfiguration and a threadpool sizing to adjust based on machine size
* *controller*-http API call interfaces
* *data*-code to generate POC type of customer, account, and transaction code
* *domain*-has each of the java objects with their columns.  Enables all the getter/setter methods
* *repository*-This is repository layer
* *service*-asyncservice and bankservice doing the interaction with redis

The java code demonstrates common API actions with the data layer in REDIS.  The java spring Boot framework minimizes the amount of code to build and maintain this solution.  Maven is used to build the java code and the code is deployed to the tomcat server.

### Data Structures
<a href="" rel="Tables Structures Used"><img src="images/Tables.png" alt="" /></a>

## Instructions

### Multiple options for creating the environment:
* run with docker-compose using a flask and redis container
* installing for mac os
* running on linux (probably in the cloud)
* running on kubernetes (example uses GKE)

#### Run in Docker Compose
* Docker installed on your local system, see [Docker Installation Instructions](https://docs.docker.com/engine/installation/).
* Can run docker using application in docker hub (commented out in docker-compose.yaml)
* When using Docker for Mac or Docker for Windows, the default resources allocated to the linux VM running docker are 2GB RAM and 2 CPU's. Make sure to adjust these resources to meet the resource requirements for the containers you will be running. More information can be found here on adjusting the resources allocated to docker.
[Docker for mac](https://docs.docker.com/docker-for-mac/#advanced)
[Docker for windows](https://docs.docker.com/docker-for-windows/#advanced)
#### Run on local machine without docker
* Still run the redis on docker but run the application in intelli4j or from jar command
* Must set environment variables
#### Run docker hub container and redis enterprise on kubernetes
* Yaml files provided

## Create Environment

This github java code uses the redisson java library with spring boot starter for redis.
```bash
git clone https://github.com/jphaugla/Redis-Digital-Banking-redisson.git
```
### Docker Compose 
* Refer to the notes for redis Docker images used but don't get too bogged down as docker compose handles everything except for a few admin steps on tomcat.
 * [Redis Stack](https://redis.io/docs/stack/get-started/install/docker/)  
* Open terminal and change to the github home where you will see the docker-compose.yml file, then: 
```bash
docker-compose up -d
```
#### Notes on this docker-compose setup
* this environment has 4 docker containers, 3 redis containers (redis, redis2, redis3).  redis2 and redis3 are replicas of the primary redis instance in the container simply named "redis".
* redisson splits read load evenly across the three redis containers
* the redisson based java application can be built using "docker compose build"
* this is just one use case for redisson.  Redisson configuration is controlled with the *REDISSON_YAML_PATH* environment variable

### Deploy Linux
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
git clone https://github.com/jphaugla/Redis-Digital-Banking-redisson.git
```

#### Compile the application 

* Compile the code
```bash
mvn package
```
#### Set Environment
| variable               | Original Value         | Desccription                                                                                           |
|------------------------|------------------------|--------------------------------------------------------------------------------------------------------|
| REDIS_CONNECTION       | redis://redis:6379     | redis connection string                                                                                |
| REDIS_REPLICA1         | redis://redis:6380     | redis replica1 connection string (only used if using replica)                                          |
| REDIS_REPLICA2         | redis://redis:6381     | redis replica2 connection string (only used if using replica)                                          |
| CORE_POOL_SIZE         | 20                     | On larger machines, increasing this will increase load speed                                           |
| REDISSON_YAML_PATH     | /etc/redisson-ssl.yaml | Full path to redisson yaml file.  Documentation provided below                                         |
| ---------------------- | -----------------      | ------------------------------------------------------------------------------------------------------ |
* run the jar file after setting up required environment variables. 
* Also have set up a script to do these environment variables instead of typing this under ./scripts/setEnv.sh to use this
#### Redisson yaml path
The redisson yaml controls the behavior of redisson with the shards-redisson documentation for the [redisson yaml](https://github.com/redisson/redisson/wiki/2.-Configuration)
Several samples are provided in this github.
##### [Basic redisson configuration](https://github.com/jphaugla/Redis-Digital-Banking-redisson/blob/main/src/main/resources/redisson.yaml)
```bash
singleServerConfig:
  address: ${REDIS_CONNECTION}
  password: ${REDIS_PASSWORD}
#  can un-comment this out if  using an ACL
  # username: ${REDIS_USERNAME}
```
* more documentation on [basic configuration](https://github.com/redisson/redisson/wiki/2.-Configuration#26-single-instance-mode)
* works with redis enterprise or with single OSS redis node
##### [SSL redisson configuration](https://github.com/jphaugla/Redis-Digital-Banking-redisson/blob/main/src/main/resources/redisson-ssl.yaml)
```bash
singleServerConfig:
  address: ${REDIS_CONNECTION}
  password: ${REDIS_PASSWORD}
#  can un-comment this out if  using an ACL
  # username: ${REDIS_USERNAME}
  sslProtocols:
    - TLSv1.3
    - TLSv1.2
    - TLSv1.1
    - TLSv1
  sslProvider: JDK
  sslKeystore: file:./src/main/resources/ssl/client-keystore.p12
  sslKeystorePassword: ${KEYSTORE_PASSWORD}
  sslTruststore: file:./src/main/resources/ssl/client-truststore.p12
  sslTruststorePassword: ${TRUSTSTORE_PASSWORD}
```
##### [Cluster redisson configuration](https://github.com/jphaugla/Redis-Digital-Banking-redisson/blob/main/src/main/resources/redisson-cluster.yaml)
```bash
clusterServersConfig:
  nodeAddresses:
  - ${REDIS_CONNECTION}
  password: ${REDIS_PASSWORD}
#  can un-comment this out if  using an ACL
  # username: ${REDIS_USERNAME}
  readMode: MASTER
  subscriptionMode: MASTER
```
* more documentation on [cluster configuration](https://github.com/redisson/redisson/wiki/2.-Configuration#24-cluster-mode)
* works with redis enterprise in OSS API mode or with AWS Elasticache
##### [Master Slave Mode configuration](https://github.com/jphaugla/Redis-Digital-Banking-redisson/blob/main/src/main/resources/redisson-replica.yaml)
```bash
masterSlaveServersConfig:
  masterAddress: ${REDIS_CONNECTION}
  readMode: ${READ_MODE}
  subscriptionMode: "SLAVE"
  password: ${REDIS_PASSWORD}
  slaveAddresses:
    - ${REDIS_REPLICA1}
    - ${REDIS_REPLICA2}
```
* more documentation on [MasterSlave Mode](https://github.com/redisson/redisson/wiki/2.-Configuration#28-master-slave-mode)
* works with replica of nodes
#####[Sentinel configuration](https://github.com/redisson/redisson/wiki/2.-Configuration#27-sentinel-mode)
```bash
sentinelServersConfig:
  sentinelAddresses: ${REDIS_SENTINEL_CONNECTION}
  username: ${REDIS_USERNAME}
  password: ${REDIS_PASSWORD}
  masterName: "db1""
```
```bash
source ./scripts/setEnv.sh
```
* alternatively, these are the commands
### Redisson yaml files
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
### Kubernetes
#### Install Redis Enterprise
* One option is GKE cluster, use at least e2-standard-4 nodes
* Follow [Redis Enterprise k8s installation instructions](https://github.com/RedisLabs/redis-enterprise-k8s-docs#installation) all the way through to step 4.
* For Step 5, the admission controller steps are not really needed and the webhook instructions are not necessary
* Don't do Step 6 as the databases for this github are in the k8s subdirectory of this github
#### Create database
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
* click on the Configuration tab
* Click on the *Get Replica of source URL" and copy it to the clipboard
* <a href="" rel="Generate Data Benchmark"><img src="images/getreplicaURL.png" alt="" /></a>
* this URL needs to be turned into a secret using base64.  Substitute the captured uri for this uri
```bash
echo redis://admin:Cl5rhnQcAXTK21JKxxxxxxxxxFnLPjKIZy89CsqAABTKOK3v@redis-14376.rec.demo.svc.cluster.local:14376 |base64
```
* paste this secret into the urlSecret.yml file replacing the existing secret value behind the "uri:" key
* create this secret
```bash
kubectl apply -f urlSecret.yml
```
* the other two database files can now be applied.
  * NOTE:  the database secret name for redis2 and redis3 are changed to be the same secret that was automatically created for the primary database.  This was done because redisson only has one parameter for database password so all have to be the same
  * redis2 and redis3 also use the secret created above for the primary database uri
```bash
kubectl apply -f redis2.yml
kubectl apply -f redis3.yml
```
#### Add redisinsights
These instructions are based on [Install RedisInsights on k8s](https://docs.redis.com/latest/ri/installing/install-k8s/)
&nbsp;
The above instructions have two options for installing redisinights, this uses the second option to install[ without a service](https://docs.redis.com/latest/ri/installing/install-k8s/#create-the-redisinsight-deployment-without-a-service) (avoids creating a load balancer)
* switch back to demo namespace
* create redisinsights
```bash
kubectl config set-context --current --namespace=demo 
kubectl apply -f redisinsight.yaml
kubectl port-forward deployment/redisinsight 8001
```
* from chrome or firefox open the browser using http://localhost:8001
* Click "I already have a database"
* Click "Connect to Redis Database"
* Create Connection to target redis database with following parameter entries

| Key      | Value                                     |
|----------|-------------------------------------------|
| host     | redis-enterprise-database.demo            |
| port     | 18154 (get from ./getDatabasepw.sh above) |
| name     | TargetDB                                  |
| Username | (leave blank)                             |
| Password | DrCh7J31 (from ./getDatabasepw.sh above) |
* click ok


## Deploy bankapp on Kubernetes
* must log into docker to have access to the docker image
```bash
docker login
```
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

### Use the Application
*  Test the application from a separate terminal window.  This script uses an API call to generate sample banking customers, accounts and transactions.  It uses Spring ASYNC techniques to generate higher load.  A flag chooses between running the transactions pipelined in Redis or in normal non-pipelined method.
```bash
./scripts/generateData.sh
```
Shows a benchmark test run of  generateData.sh on GCP servers.  Although, this test run is using redisearch 1.0 code base.  Need to rerun this test.
<a href="" rel="Generate Data Benchmark"><img src="images/Benchmark.png" alt="" /></a>


##  Investigate the APIs 
These scripts are in ./scripts
  mark  transactions to be in a buckets such as Travel or Food for budgetary tracking purposes
  * generateData.sh - simple API to generate default customer, accounts, merchants, phone numbers, emails and transactions
  * generateLots.sh - for server testing to generate higher load levels.  Use with startAppservers.sh.  Not for use with docker setup.  This is load testing with redis enterprise and client application running in same network in the cloud.
  * generateRedisson.sh - writes a few commands using a base redisson connection outside of redistemplate
  * getCustomerByEmails.sh - gets email for customer ids starting from 100001 for the desired range - this is used for generating read load
  * getCustomerByPhone.sh - gets phone numbers for customer ids starting from 100001 for the desired range - this is used for generating read load
  * getCustomers.sh - gets a range of customer id starting from 100001 for the desired range - this is used for generating read load
  * getCustomerEmails.sh - gets email for customer ids starting from 100001 for the desired customer range - this is used for generating read load
  * getCustomerPhones.sh - gets phone numbers  for customer ids starting from 100001 for the desired range - this is used for generating read load
  * getByCustID.sh - retrieve transactions for customer
  * getByEmail.sh - retrieve customer record using email address
  * getByPhone.sh - get customers by phone only
  * getTransaction.sh - get one transaction by its transaction ID
  * loop.sh - used to load testing to keep infinite loop running.  pass parameter of script to run
  * putCustomer.sh - put a set of json customer records
  * saveAccount.sh - save a sample account
  * saveCustomer.sh - save a sample customer
  * saveTransaction.sh - save a sample Transaction
  * startAppservers.sh - start multiple app server instances for load testing
  * testPipeline.sh - test pipelining

## Generate Load
* to generate data, use generateData.sh
* to generate load, use the loop.sh script to create an infinite loop.  Pass a parameter of the script to use for the load
```bash
./generateData.sh
./loop.sh getCustomers.sh
```
```bash
./loops.sh getCustomerEmails.sh
```

## Redisson with TLS

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
## Redisson Client Side Caching 
This is a client side caching demo in a subdirectory of this github called "simple".   See redisson caching links provided toward the bottom of the [Important Links](#important-linksnotes)
### Redisson Pro or open source
* When using redisson, be aware of what can be accomplished with open source redisson and where the line is crossed to redisson pro.
  * [Redisson open source and pro features](https://redisson.pro/#compare-editions)
* The [pom.xml](simple/pom.xml) has the opensource redisson dependency commented out and the redisson pro part in place
* For redisson pro, edit the [redisson.yaml](src/main/resources/redisson.yaml) file adding the registration key
### Redisson Client Side Caching Instructions
#### move to subfolder
```bash
cd simple
```
#### Set Client Side Caching Environment
The same environment variable file is used for this subdirectory.
```bash
source ../scripts/setEnv.sh
```

#### Edit test programs to run
The [main program](simple/src/main/java/com/jphaugla/redisson/client/redissonStress.java) creates a redisson connection, calls test routines, and closes the connection.  It is a quick edit to uncomment/comment the tests to run
* Edit above program for tests to run
#### Compile Client Side Caching
```
mvn package
```
#### Run the sample program
```bash
java -jar target/redissonStress-1.0-SNAPSHOT-jar-with-dependencies.jar
```
