package com.recsys.common;

import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "profile")
public class MongoConfig {
	private String id;
	
	public String setId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
    @Bean
    public MongoClientOptions mongoOptions() {
    	int connection = 200;
    	
    	if (id != null && "stage".equals(id)) {
    		connection = 5;
    	}
    	
        return MongoClientOptions.builder()
                .connectionsPerHost(connection)
                .readPreference(ReadPreference.secondaryPreferred())
                .threadsAllowedToBlockForConnectionMultiplier(20)
                .build();
    }

}
