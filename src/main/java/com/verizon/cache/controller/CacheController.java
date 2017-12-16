package com.verizon.cache.controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verizon.cache.domain.GetFromCache;
import com.verizon.cache.domain.PushToCache;
import com.verizon.cache.services.GetFromCacheHome;
import com.verizon.cache.services.PushToCacheHome;

import redis.clients.jedis.Jedis;


@RestController
@Component
public class CacheController {
	
	private static final Log log = LogFactory
			.getLog(CacheController.class);
	GetFromCacheHome getFromCacheHome = new GetFromCacheHome();
	
	private String myRedisHostname;
	
	private String myRedisPort;
	Jedis jedis;
	
	
	@Autowired
	public CacheController(@Value("${my.redis.hostname}") String myRedisHostname, @Value("${my.redis.port}") String myRedisPort ) {

		if (myRedisHostname == null || myRedisHostname.isEmpty()){
			myRedisHostname = new String("localhost");
		}
		
		
		if (myRedisPort == null || myRedisPort.isEmpty()){
			myRedisPort = new String("6379");
		}
		
		this.myRedisHostname = myRedisHostname;
		this.myRedisPort = myRedisPort;
		log.info("using Redis hostname [" + this.myRedisHostname + "] : Port [" + this.myRedisPort + "] to connect" );
		this.jedis = new Jedis(myRedisHostname, Integer.parseInt(myRedisPort));
	}


	 
	
	@RequestMapping(value = "/getIdsToPush", method = RequestMethod.GET)
	public FormDataWrapper getIdsToPush(@RequestHeader(value = "Referer") String currentHtml){
		
    	FormDataWrapper nameValuePairs = new FormDataWrapper(); 
    	PushToCacheHome pushToCacheHome = new PushToCacheHome();
    	ArrayList<FormData> listFormData = new ArrayList<FormData>();
    	
    	List<PushToCache> results = pushToCacheHome.getByHtmlName(
    			Paths.get(currentHtml).getFileName().toString());
    	
    	for(PushToCache pushToCache : results){
    		FormData fd = new  FormData();
    		fd.name = "ID";
    		fd.value = pushToCache.getId().getFieldId();
    		listFormData.add(fd);
    	}
	
    	nameValuePairs.setFormData(listFormData);
    	return nameValuePairs;		
	}
	
	@RequestMapping(value = "/getDataFromCache", method = RequestMethod.GET)
    public FormDataWrapper getFromCache(@RequestHeader(value = "Referer") String currentHtml){
		
		GetFromCacheHome getFromCacheHome = new GetFromCacheHome();

		FormDataWrapper nameValuePairs = new FormDataWrapper(); 
		ArrayList<FormData> listFormData = new ArrayList<FormData>();
   	
    	List<GetFromCache> results = getFromCacheHome.getByHtmlName(
    			Paths.get(currentHtml).getFileName().toString());
    	for (GetFromCache getFromCache : results){
    		FormData formData = new FormData();
    		formData.setName(getFromCache.getId().getFieldId());
    		String fieldValue = jedis.hget("user#1", getFromCache.getId().getParameterName());
    		log.info("Got from Redis [ParameterName -> " + getFromCache.getId().getParameterName() + " <---> Value -> " + fieldValue + "]");
    		formData.setValue(fieldValue);
    		listFormData.add(formData);
    	}

    	nameValuePairs.setFormData(listFormData);
    	return nameValuePairs;
	}

	
    @RequestMapping(value = "/pushDataToCache", method = RequestMethod.POST)
    public void pushToCache(@RequestBody FormDataWrapper fromObj,
    		@RequestHeader(value = "Referer") String currentHtml){
		
		PushToCacheHome putToCacheHome = new PushToCacheHome();
		HashMap<String, String> idToName = new HashMap<String, String>();
   	
    	List<PushToCache> results = putToCacheHome.getByHtmlName(
    			Paths.get(currentHtml).getFileName().toString());
    	for (PushToCache putToCache : results){
    		idToName.put(putToCache.getId().getFieldId(), putToCache.getId().getParameterName());
    	}

    	log.info("JSON Success " + fromObj.getFormData().size());
    	    	
    	for (FormData formData: fromObj.getFormData()){
    		log.info("Id -> " + formData.getName() + " <---> Value -> " + formData.getValue());
    		jedis.hset("user#1", idToName.get(formData.getName()), formData.getValue());
    		log.info("Pushing to Redis [ParameterName -> " + idToName.get(formData.getName()) + " <---> Value -> " + formData.getValue() + "]");
    	} 
    }
}
