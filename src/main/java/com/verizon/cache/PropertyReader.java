package com.verizon.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.verizon.cache.controller.CacheController;

@Component

public class PropertyReader {
	private final String myRedisHostname;
	private final String myRedisPort;
	private static final Log log = LogFactory
			.getLog(PropertyReader.class);
	

	@Autowired
    public PropertyReader(@Value("${my.redis.hostname}") String myRedisHostname, @Value("${my.redis.port}") String myRedisPort) {
        this.myRedisHostname = myRedisHostname;
        this.myRedisPort = myRedisPort;
        log.info("using Redis hostname [" + this.myRedisHostname + "] : Port [" + this.myRedisPort + "] to connect" );
        
    }

	public String getMyRedisHostname() {
		return myRedisHostname;
	}

	public String getMyRedisPort() {
		return myRedisPort;
	}
}