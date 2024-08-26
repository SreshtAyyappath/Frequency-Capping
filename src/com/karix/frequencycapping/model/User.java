package com.karix.frequencycapping.model;

import java.util.Date;

import com.karix.frequencycapping.dao.UserRedis;

public class User {
//	int mobileNumber;
//	int dailyCounter;
//	int weeklyCounter;
//	int monthlyCounter;
//	long currentDate;
// 	long startDate;
// 	HashSet <int, int>
// 	int toBeAddedToWeeklyCounter;
// 	int toBeAddedToMonthlyCounter;
	Customer customer;
	public UserRedis userRedis;
	
	
	public User(Customer customer, String mobileNumber, Date startDate, Date currentDate) {
		userRedis = new UserRedis();
		this.customer = customer;
		if(!userRedis.isPresent(customer, mobileNumber))
			userRedis.addUser(customer, mobileNumber, startDate, currentDate);
	}
	
	public void printAllCounters(String mobileNumber) {
		userRedis.printAllCounters(customer, mobileNumber);
	}
	
	public boolean canSendMessage(String mobileNumber, Date date) {
		return userRedis.canSendMessage(customer, mobileNumber, date);
	}

}
