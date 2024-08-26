package com.karix.frequencycapping;

import java.util.Calendar;
import java.util.Date;

import com.karix.frequencycapping.model.Customer;
import com.karix.frequencycapping.model.User;
import com.karix.redis.cluster.utility.RedisReader;
import com.karix.redis.cluster.utility.RedisWriter;
import com.mgage.middleware.common.connectionpool.InitializeConnectionPool;

public class Main {

	public static void main(String[] args) throws Exception {

		
		InitializeConnectionPool.start();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2023, Calendar.DECEMBER, 28);
		
		Date startDate = calendar.getTime();
		
		
		for(int i = 1; i <= 30; i++) {
			Date date = calendar.getTime();
			Customer customer = new Customer("800588", 10, 50, 200);
			User user = new User(customer, "9309851910", startDate, date);
			System.out.print("Day: " + date + " -- ");
			for(int j = 0; j < 10; j++) {
				user.canSendMessage("9309851910", date);
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		
		
		//calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date date = calendar.getTime();
		Customer customer = new Customer("800588", 10, 50, 200);
		User user = new User(customer, "9309851910", startDate, date);
		System.out.println("sending a message on the after a month");
		System.out.print("Day: " + date + " -- ");
		user.canSendMessage("9309851910", date);
		
		
		
	}

}
