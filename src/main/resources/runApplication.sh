#!/bin/bash
envsubst < /etc/redisson-replica.yaml > /etc/redisson-replica-vars.yaml
java -jar /usr/app/redis-0.0.1-SNAPSHOT.jar
