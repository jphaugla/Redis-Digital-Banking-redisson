export REDIS_CONNECTION="rediss://redis-18199.jphterra2.demo-rlec.redislabs.com:18199"
export REDIS_HOST="redis-18199.jphterra2.demo-rlec.redislabs.com"
export REDIS_PORT=18199
export REDIS_SENTINEL_PORT=8001
# export REDIS_CONNECTION="redis://redis-10288.jphterra2.demo-rlec.redislabs.com:10288"
# export REDIS_SENTINEL_CONNECTION="redis://redis-10288.jphterra2.demo-rlec.redislabs.com:8001"
export REDIS_SENTINEL_CONNECTION="rediss://redis-18199.jphterra2.demo-rlec.redislabs.com:8001"
# export REDIS_CONNECTION="redis://localhost:6379"
export REDIS_REPLICA1="rediss://localhost:6380"
export REDIS_REPLICA2="rediss://localhost:6381"
export CORE_POOLSIZE=20
export READ_MODE=MASTER_SLAVE
export REDIS_USERNAME=jph
export REDIS_PASSWORD=redis123
# export REDISSON_YAML_PATH=src/main/resources/redisson-replica.yaml
# export REDISSON_YAML_PATH=src/main/resources/redisson-sentinel.yaml
export REDISSON_YAML_PATH=src/main/resources/redisson-sentinel-ssl.yaml
# export REDISSON_YAML_PATH=src/main/resources/redisson-ssl.yaml
# export REDISSON_YAML_PATH=src/main/resources/redisson.yaml
export REDISSON_CACHE_YAML_PATH=src/main/resources/cache-config.yaml
export KEYSTORE_PASSWORD=jasonrocks
export TRUSTSTORE_PASSWORD=jasonrocks
export READ_COUNT=200
export WRITE_COUNT=200
