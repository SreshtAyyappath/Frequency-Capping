package com.karix.frequencycapping.dao;

import com.karix.redis.cluster.utility.RedisReader;
import com.karix.redis.cluster.utility.RedisWriter;

public class CustomerRedis {
	public static final String redisFeature = "MNP";
	
	
	public void setDailyCap(String ESME, int dailyCap) {
		try {
			RedisWriter.HSetToCluster(redisFeature, "FREQUENCYCAPPING:Customers:" + ESME, "dailyCap", Integer.toString(dailyCap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void setWeeklyCap(String ESME, int weeklyCap) {
		try {
			RedisWriter.HSetToCluster(redisFeature, "FREQUENCYCAPPING:Customers:" + ESME, "weeklyCap", Integer.toString(weeklyCap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setMonthlyCap(String ESME, int monthlyCap) {
		try {
			RedisWriter.HSetToCluster(redisFeature, "FREQUENCYCAPPING:Customers:" + ESME, "monthlyCap", Integer.toString(monthlyCap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getDailyCap(String ESME) {
		int dailyCap = Integer.parseInt(RedisReader.getHGetValueFromCluster(redisFeature, "FREQUENCYCAPPING:Customers:" + ESME, "dailyCap"));
		return dailyCap;
	}
	
	public int getWeeklyCap(String ESME) {
		int weeklyCap = Integer.parseInt(RedisReader.getHGetValueFromCluster(redisFeature, "FREQUENCYCAPPING:Customers:" + ESME, "weeklyCap"));
		return weeklyCap;
	}
	
	public int getMonthlyCap(String ESME) {
		int monthlyCap = Integer.parseInt(RedisReader.getHGetValueFromCluster(redisFeature, "FREQUENCYCAPPING:Customers:" + ESME, "monthlyCap"));
		return monthlyCap;
	}
	
}
