package com.jphaugla.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;


/**
 * @author aterleto
 *
 * <br><br>
 * Utility configuration class to Create and Shutdown Redisson's client proxy
 *
 */
public class RedissonClientConfig {

	private RedissonClientConfig() {}
	
	static public RedissonClient createRedissonClient(Config config) {
		
		RedissonClient redissonClient = null;	
		try {
			// config.setRegistrationKey("aJQJBRaetQQ94bXEvDkxdVbi18adrnSFJMIGgDQnO9p+udyY7JB7E4W8uk4VN/mZveGU2uszpqy38/IChepIw78osZRSHKrOY/dy8cYHh82wQUYJDmMFQzNDpekmpMAs1lFVZDUn/RjJLax35tN+tzeJwXiYONWJsVBDanfew1M=");

			redissonClient = Redisson.create(config);
						
		}catch(Exception e) {
	    	System.out.println(e);
		}finally {
			shutdown(redissonClient);
		}
		
		return redissonClient;
	}
	
	static public RedissonClient createRedissonClient(String[] args) {
		
		RedissonClient redissonClient = null;	
		try {	
			redissonClient = Redisson.create(configureRedissonClient(args));
		}catch(Exception e) {
	    	System.out.println(e);
	    	//TODO: Rethrow checked exception
		}
		
		return redissonClient;
	}
	
	static public Config configureRedissonClient(String[] args) {
		
		String hostname = "localhost";
		int port = 6376;
		boolean isClustered = true;
		
		if(args != null) {
			
			int argsCount = args.length;
			
			if(argsCount == 3) {
				isClustered = new Boolean(args[0]);
				hostname = args[1];
				port = new Integer(args[2]);
			}
			else if(argsCount == 2) {
				isClustered = new Boolean(args[0]);
				hostname = args[1];
			}

		}
				
		Config config = new Config();
		
		// config.setRegistrationKey("aJQJBRaetQQ94bXEvDkxdVbi18adrnSFJMIGgDQnO9p+udyY7JB7E4W8uk4VN/mZveGU2uszpqy38/IChepIw78osZRSHKrOY/dy8cYHh82wQUYJDmMFQzNDpekmpMAs1lFVZDUn/RjJLax35tN+tzeJwXiYONWJsVBDanfew1M=");
		
		if(isClustered) {
			config.useClusterServers().setScanInterval(2000) // Cluster state scan interval in milliseconds
			.addNodeAddress("redis://" + hostname + ":" + port); //Redisson automatically discovers the remaining cluster nodes
		}else {
			config.useSingleServer().setAddress("redis://" + hostname + ":" + port);
		}
		
		return config;
	}
		
	static public void shutdown(RedissonClient redissonClient) {
		if(redissonClient != null)
			redissonClient.shutdown();
		redissonClient = null;
	}
	
}