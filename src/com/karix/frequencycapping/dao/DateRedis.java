package com.karix.frequencycapping.dao;

import java.util.Date;

import com.karix.redis.cluster.utility.RedisReader;
import com.karix.redis.cluster.utility.RedisWriter;

public class DateRedis {
	
	public void setCount(String ESME, String mobileNumber, Date date, int count){
		long dateInMilliseconds = date.getTime();
		try {
		RedisWriter.SetToCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:Dates:" + ESME + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()) + ":" + Long.toString(dateInMilliseconds), Integer.toString(count));
		RedisReader.expireFormCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:Dates:" + ESME + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()) + ":" + Long.toString(dateInMilliseconds), (31*24*60*60));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getCount(String ESME, String mobileNumber, Date date) {
		if(!isDatePresent(ESME, mobileNumber, date)) {
			return 0;
		}
		long dateInMilliseconds = date.getTime();
		String count = RedisReader.GetFormCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:Dates:" + ESME + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()) + ":" + Long.toString(dateInMilliseconds));
		return Integer.parseInt(count);
	}
	
	public boolean isDatePresent(String ESME, String mobileNumber, Date date) {
		long dateInMilliseconds = date.getTime();
		return RedisReader.existsFormCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:Dates:" + ESME + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()) + ":" + Long.toString(dateInMilliseconds));
	}
	
}
