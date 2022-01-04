package com.jphaugla.repository;

import com.jphaugla.domain.Phone;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
@Repository

public class PhoneRepository{
	private static final String KEY = "Phone";


	final Logger logger = LoggerFactory.getLogger(com.jphaugla.repository.PhoneRepository.class);
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	@Qualifier("redisTemplateW1")
	private RedisTemplate<Object, Object> redisTemplateW1;

	@Autowired
	@Qualifier("stringRedisTemplateW1")
	private StringRedisTemplate stringRedisTemplate;

	public PhoneRepository() {

		logger.info("PhoneRepository constructor");
	}

	public String create(Phone phone) {

		Map<Object, Object> phoneHash = mapper.convertValue(phone, Map.class);
		String phoneKey = "Phone:" + phone.getPhoneNumber();
		redisTemplateW1.opsForHash().putAll(phoneKey, phoneHash);
		// for demo purposes add a member to the set for the Customer
		stringRedisTemplate.opsForSet().add("CustPhone:" + phone.getCustomerId(), phone.getPhoneNumber());
		// redisTemplate.opsForHash().putAll("Phone:" + phone.getPhoneId(), phoneHash);
		// logger.info(String.format("Phone with ID %s saved", phone.getPhoneNumber()));
		return "Success\n";
	}

	public Phone get(String phoneId) {
		// logger.info("in Phone Repository.get with phone id=" + phoneId);
		Map<Object, Object> phoneHash = stringRedisTemplate.opsForHash().entries(phoneId);
		// logger.info("Full key is " + phoneId + " phoneHash is " + phoneHash);
		Phone phone = mapper.convertValue(phoneHash, Phone.class);
		// logger.info("return phone " + phone.getPhoneNumber() + ":" + phone.getPhoneLabel() + ":" + phone.getCustomerId());
		return (phone);
	}


}
