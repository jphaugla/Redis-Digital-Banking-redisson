package com.jphaugla.redisson.client;

import com.jphaugla.redisson.config.Timer;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author jphaugland
 *
 * <br><br>
 * Basic read operation using Redisson's RList representing Redis' List data structure
 *
 */
public class redissonStress {
	private static List<Map<String,String>> maps = new ArrayList<Map<String,String>>();
	public static void main(String[] args) {
		
		RedissonClient redissonClient = null;
		String read_count = System.getenv("READ_COUNT");
		String write_count = System.getenv("WRITE_COUNT");
		System.out.println("Starting main with read count of " + read_count + " write count of " + write_count);
		int number_to_write = Integer.parseInt(write_count);
		int number_to_read = Integer.parseInt(read_count);
		try {
			Config config = Config.fromYAML(new File("src/main/resources/redisson.yaml"));
			redissonClient = Redisson.create(config);

			// writeNumbers(redissonClient, number_to_write);
			// writeAddressMap(redissonClient, number_to_write);
			// readAddressMap(redissonClient, number_to_write, number_to_read);
			//
			writeLocalCache(redissonClient, number_to_write);
			readLocalCache(redissonClient, number_to_write, number_to_read);
			// writeReadLocalCache(redissonClient, number_to_write, number_to_read);
			// cacheExample(redissonClient);
			

		}catch(Exception e) {
	    	System.out.println(e);
		}finally {
			redissonClient.shutdown();
		}
		
   }
   private static void writeAddressMap(RedissonClient redissonClient, int number_to_write) {
	   System.out.println("Started writeAdddressMap");
	   Timer timer = new Timer();
	   for (int i =0; i < number_to_write; i++) {

		   String stringValue = String.valueOf(i);
		   String person_index = "Person:" + String.valueOf(i);
		   RMap<String, String> map =  redissonClient.getMap(person_index);
		   map.fastPut ("FirstName", "jason" + stringValue);
		   map.fastPut("MiddleName", "oliver" + stringValue);
		   map.fastPut ("LastName", "haugland" + stringValue);
		   map.fastPut ("Position", "Employee");
		   int age = 25 + (i / 2);
		   String age_string = String.valueOf(age);
		   map.fastPut ("age", age_string);
	   }
	   timer.end();
	   System.out.println("Finished writeAddress Map writing " + number_to_write + " created in " +
			   timer.getTimeTakenMillis() + " milli seconds.");
   }
   private static LocalCachedMapOptions getOptions() {
	   LocalCachedMapOptions options = LocalCachedMapOptions.defaults()

			   // Defines whether to store a cache miss into the local cache.
			   // Default value is false.
			   .storeCacheMiss(false)

	   // Defines store mode of cache data.
	   // Follow options are available:
	   // LOCALCACHE - store data in local cache only and use Redis only for data update/invalidation.
	   // LOCALCACHE_REDIS - store data in both Redis and local cache.
               .storeMode(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS)

			   // Defines Cache provider used as local cache store.
			   // Follow options are available:
			   // REDISSON - uses Redisson own implementation
			   // CAFFEINE - uses Caffeine implementation
			   .cacheProvider(LocalCachedMapOptions.CacheProvider.REDISSON)

			   // Defines local cache eviction policy.
			   // Follow options are available:
			   // LFU - Counts how often an item was requested. Those that are used least often are discarded first.
			   // LRU - Discards the least recently used items first
			   // SOFT - Uses weak references, entries are removed by GC
			   // WEAK - Uses soft references, entries are removed by GC
			   // NONE - No eviction
			   .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.NONE)

			   // If cache size is 0 then local cache is unbounded.
			   .cacheSize(100000)

			   // Defines strategy for load missed local cache updates after Redis connection failure.
			   //
			   // Follow reconnection strategies are available:
			   // CLEAR - Clear local cache if map instance has been disconnected for a while.
			   // LOAD - Store invalidated entry hash in invalidation log for 10 minutes
			   //        Cache keys for stored invalidated entry hashes will be removed
			   //        if LocalCachedMap instance has been disconnected less than 10 minutes
			   //        or whole cache will be cleaned otherwise.
			   // NONE - Default. No reconnection handling
			   .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE)

			   // Defines local cache synchronization strategy.
			   //
			   // Follow sync strategies are available:
			   // INVALIDATE - Default. Invalidate cache entry across all LocalCachedMap instances on map entry change
			   // UPDATE - Insert/update cache entry across all LocalCachedMap instances on map entry change
			   // NONE - No synchronizations on map changes
			   .syncStrategy(LocalCachedMapOptions.SyncStrategy.NONE)

			   // or
			   .timeToLive(100, TimeUnit.SECONDS)

			   // max idle time for each map entry in local cache
			   // or
			   .maxIdle(100, TimeUnit.SECONDS);
	   return options;
   }

	private static void readAddressMap(RedissonClient redissonClient, int number_available, int number_to_read) {
		System.out.println("Started readAddressMap");
		Timer timer = new Timer();
		String first_name = "";
		String middle_name = "";
		String last_name = "";
		String position ="";
		int age = 0;
		String age_string = "";
		for (int i =0; i < number_to_read; i++) {

			String person_numeric = String.valueOf(getRandomNumber(0,number_available-1));
			String person_index = "Person:" + person_numeric;
			RMap<String, String> map =  redissonClient.getMap(person_index);
			first_name = map.get("FirstName");
			// middle_name = map.get("MiddleName");
			// last_name = map.get("LastName");
			// position = map.get("Position");
			// age_string = map.get("age");
			// age = Integer.parseInt(age_string);
			// System.out.println(" reading readAddressMap " + person_index + " got first_name " + map.get("FirstName"));
		}
		timer.end();
		System.out.println("Finished readAddress Map reading " + number_to_read + " completed in " +
				timer.getTimeTakenMillis() + " milli seconds.");

	}
	private static void readLocalCache(RedissonClient redissonClient, int number_available, int number_to_read) {
		System.out.println("Started readLocalCache");
		Timer timer = new Timer();
		String first_name = "";
		String middle_name = "";
		String last_name = "";
		String position ="";
		int age = 0;
		String age_string = "";
		int null_count = 0;

		for (int i =0; i < number_to_read; i++) {

			int person_numeric = getRandomNumber(0,number_available-1);
			String person_string = String.valueOf(person_numeric);
			// String person_index = "Person:Cached:" + person_numeric;
			// RLocalCachedMap<String, String> map = redissonClient.getLocalCachedMap(person_index, getOptions());
			Map<String, String> localMap = maps.get(person_numeric);
			// cache.values();
			first_name = localMap.get("FirstName");

			if(first_name == null) null_count++;
			// middle_name = map.get("MiddleName");
			// last_name = map.get("LastName");
			// position = map.get("Position");
			// age_string = map.get("age");
			// age = Integer.parseInt(age_string);
			// System.out.println(" reading readLocalCache index " + person_string + " localMap.size " + String.valueOf(localMap.size()) + " first name " + localMap.get("FirstName"));
		}
		timer.end();
		System.out.println("Finished readLocalCache Map reading " + number_to_read + " completed in " +
				timer.getTimeTakenMillis() + " milli seconds. null count is " + null_count);

	}
	public static int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	private static void writeReadLocalCache(RedissonClient redissonClient, int number_to_write, int number_to_read) {
		Timer timer = new Timer();
		System.out.println("Started writeReadLocalCache");
		for (int i =0; i < number_to_write; i++) {

			String stringValue = String.valueOf(i);
			String person_index = "Person:Cached:" + String.valueOf(i);
			RLocalCachedMap<String, String> map =  redissonClient.getLocalCachedMap(person_index, getOptions());
			Map<String, String> cache = map.getCachedMap();
			map.fastPut ("FirstName", "jason" + stringValue);
			map.fastPut("MiddleName", "oliver" + stringValue);
			map.fastPut ("LastName", "haugland" + stringValue);
			map.fastPut ("Position", "Employee");
			int age = 25 + (i / 2);
			String age_string = String.valueOf(age);
			map.fastPut ("age", age_string);
		}

		timer.end();
		System.out.println("Finished writing in writeReadLocalCache " + number_to_write + " created in " +
				timer.getTimeTakenMillis() + " seconds.");
		System.out.println("Started readLocalCache");
		Timer timer2 = new Timer();
		String first_name = "";
		String middle_name = "";
		String last_name = "";
		String position ="";
		int age = 0;
		String age_string = "";

		for (int i =0; i < number_to_read; i++) {

			String person_numeric = String.valueOf(getRandomNumber(0,number_to_write-1));
			String person_index = "Person:Cached:" + person_numeric;
			RLocalCachedMap<String, String> map = redissonClient.getLocalCachedMap(person_index, LocalCachedMapOptions.defaults());
			Map<String, String> cache = map.getCachedMap();
			first_name = map.get("FirstName");
			// middle_name = map.get("MiddleName");
			// last_name = map.get("LastName");
			// position = map.get("Position");
			// age_string = map.get("age");
			// age = Integer.parseInt(age_string);
			// System.out.println(" reading readLocalCache " + person_index + " got first_name " + first_name);
		}
		timer2.end();
		System.out.println("Finished readLocalCache Map reading " + number_to_read + " completed in " +
				timer2.getTimeTakenMillis() + " milli seconds.");
	}

   private static void writeLocalCache(RedissonClient redissonClient, int number_to_write) {
		Timer timer = new Timer();
	    System.out.println("Started writeLocalCache");
	    for (int i =0; i < number_to_write; i++) {

		   String stringValue = String.valueOf(i);
		   String person_index = "Person:Cached:" + String.valueOf(i);
		   RLocalCachedMap<String, String> map =  redissonClient.getLocalCachedMap(person_index, LocalCachedMapOptions.defaults());
		   map.fastPut ("FirstName", "jason" + stringValue);
		   map.fastPut("MiddleName", "oliver" + stringValue);
		   map.fastPut ("LastName", "haugland" + stringValue);
		   map.fastPut ("Position", "Employee");
		   int age = 25 + (i / 2);
		   String age_string = String.valueOf(age);
		   map.fastPut ("age", age_string);
		   Map<String, String> localMap = map.getCachedMap();
		   maps.add(localMap);
		   String first_name = localMap.get("FirstName");   //  avoids read
		   // System.out.println(" localMap size " +  String.valueOf(localMap.size()) + " firstname " + first_name);
	   }

		timer.end();
		System.out.println("Finished writing writeLocalCache " + number_to_write + " created in " +
				timer.getTimeTakenMillis() + " milli seconds.");
   }

	private static void writeMap(RedissonClient redissonClient) {
		RMap<String, Integer> map =  redissonClient.getMap("myMap");
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		map.put("d", 4);

		boolean contains = map.containsKey("a");
		System.out.println("map contains key " + contains);
		Integer value = map.get("c");
		// Integer updatedValue = map.addAndGet("a", 32);

		Integer valueSize = map.valueSize("c");
		// System.out.println("map valueSize for C " + valueSize.toString());
	}
	private static void writeNumbers(RedissonClient redissonClient, int number_to_write) {
		RList<Integer> numbers = redissonClient.getList("numbers");
		numbers.delete();
		for(int i = 0; i < number_to_write; i++)
			numbers.add(i);

		System.out.println(numbers.readAll());
	}
	private static void cacheExample(RedissonClient redissonClient) {

		LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
				.cacheSize(10000)
				.evictionPolicy(LocalCachedMapOptions.EvictionPolicy.LRU)
				.maxIdle(10, TimeUnit.SECONDS)
				.timeToLive(60, TimeUnit.SECONDS);

		RLocalCachedMap<String, Integer> cachedMap = redissonClient.getLocalCachedMap("myMap", options);
		cachedMap.put("a", 1);
		cachedMap.put("b", 2);
		cachedMap.put("c", 3);

		boolean contains = cachedMap.containsKey("a");

		Integer value = cachedMap.get("c");

		// Integer new_int = 32;
		// Float new_number = new_int.floatValue();
		// Integer updatedValue = cachedMap.addAndGet("a", new_number);

		Integer valueSize = cachedMap.valueSize("c");

		Set<String> keys = new HashSet<String>();
		keys.add("a");
		keys.add("b");
		keys.add("c");
		Map<String, Integer> mapSlice = cachedMap.getAll(keys);

		// use read* methods to fetch all objects
		Set<String> allKeys = cachedMap.readAllKeySet();
		Collection<Integer> allValues = cachedMap.readAllValues();
		Set<Map.Entry<String, Integer>> allEntries = cachedMap.readAllEntrySet();

		// use fast* methods when previous value is not required
		boolean isNewKey = cachedMap.fastPut("a", 100);
		boolean isNewKeyPut = cachedMap.fastPutIfAbsent("d", 33);
		long removedAmount = cachedMap.fastRemove("b");
	}

}
