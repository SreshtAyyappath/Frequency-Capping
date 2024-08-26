package com.karix.frequencycapping.model;

import java.util.Date;

import com.karix.frequencycapping.dao.CustomerRedis;
import com.karix.frequencycapping.dao.DateRedis;
import com.karix.redis.cluster.utility.RedisReader;

public class DateService {
	// int count;
	DateRedis dateRedis = new DateRedis();
	
	public void setCount(String ESME, String mobileNumber, Date date, int count) {
		dateRedis.setCount(ESME, mobileNumber, date, count);
	}
	
	public int getCount(String ESME, String mobileNumber, Date date) {
		return dateRedis.getCount(ESME, mobileNumber, date);
	}
	
	public boolean isDatePresent(String ESME, String mobileNumber, Date date) {
		long dateInMilliseconds = date.getTime();
		return RedisReader.existsFormCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:Dates:" + ESME + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()) + ":" + Long.toString(dateInMilliseconds));
	}
	
}
