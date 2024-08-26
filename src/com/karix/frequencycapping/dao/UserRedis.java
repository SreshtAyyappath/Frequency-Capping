package com.karix.frequencycapping.dao;


import java.util.Calendar;
import java.util.Date;

import com.karix.frequencycapping.model.Customer;
import com.karix.redis.cluster.utility.RedisReader;
import com.karix.redis.cluster.utility.RedisWriter;
import com.karix.frequencycapping.model.DateService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

public class UserRedis {
	DateService dateService = new DateService();
	
	public void addUser(Customer customer, String mobileNumber, Date startDate, Date currentDate) {
		setDailyCounter(customer, mobileNumber, customer.getDailyCap());
		setWeeklyCounter(customer, mobileNumber, customer.getWeeklyCap());
		setMonthlyCounter(customer, mobileNumber, customer.getMonthlyCap());
		setStartDate(customer, mobileNumber, startDate);
		setCurrentDate(customer, mobileNumber, currentDate);
	}
	
	public boolean isPresent(Customer customer, String mobileNumber) {
		return RedisReader.existsFormCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()));
	}
	
	public void setDailyCounter(Customer customer, String mobileNumber, int dailyCounter) {
		try {
			RedisWriter.HSetToCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "dailyCounter", Integer.toString(dailyCounter));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setWeeklyCounter(Customer customer, String mobileNumber, int weeklyCounter) {
		try {
			RedisWriter.HSetToCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "weeklyCounter", Integer.toString(weeklyCounter));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setMonthlyCounter(Customer customer, String mobileNumber, int monthlyCounter) {
		try {
			RedisWriter.HSetToCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "monthlyCounter", Integer.toString(monthlyCounter));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public int getDailyCounter(Customer customer, String mobileNumber) {
		int dailyCounter = Integer.parseInt(RedisReader.getHGetValueFromCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "dailyCounter"));
		return dailyCounter;
	}
	
	public int getWeeklyCounter(Customer customer, String mobileNumber) {
		int weeklyCounter = Integer.parseInt(RedisReader.getHGetValueFromCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "weeklyCounter"));
		return weeklyCounter;
	}
	
	public int getMonthlyCounter(Customer customer, String mobileNumber) {
		int monthlyCounter = Integer.parseInt(RedisReader.getHGetValueFromCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "monthlyCounter"));
		return monthlyCounter;
	}
	
	public void setCurrentDate(Customer customer, String mobileNumber, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
		long dateInMilliseconds = calendar.getTime().getTime();
		
		try {
			RedisWriter.HSetToCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "currentDate", Long.toString(dateInMilliseconds));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Date getCurrentDate(Customer customer, String mobileNumber) {
		long currentDateinMilliseconds = Long.parseLong(RedisReader.getHGetValueFromCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "currentDate"));
		Date currentDate = new Date(currentDateinMilliseconds);
		return currentDate;
	}
	
	public void setStartDate(Customer customer, String mobileNumber, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
		long dateInMilliseconds = calendar.getTime().getTime();
		try {
			RedisWriter.HSetToCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "startDate", Long.toString(dateInMilliseconds));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Date getStartDate(Customer customer, String mobileNumber) {
		long startDateinMilliseconds = Long.parseLong(RedisReader.getHGetValueFromCluster(CustomerRedis.redisFeature, "FREQUENCYCAPPING:MobileNumbers:" + customer.getESME() + mobileNumber.substring(0, 6) + ":" + mobileNumber.substring(6, mobileNumber.length()), "startDate"));
		Date startDate = new Date(startDateinMilliseconds);
		return startDate;
	}
	
	public void AutomaticCountersReset(final Customer customer, final String mobileNumber, final Date currentDate) {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            	Date currentDate = new Date();
                AllCountersReset(customer, mobileNumber, currentDate);
            }
        }, 0, 24, TimeUnit.HOURS);
		
		
		
	}
	public void AllCountersResetLongerMethod(Customer customer, String mobileNumber, Date currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		int messagesSentLastWeek = 0;
		int messagesSentLastMonth = 0;
		for(int i = 0; i < 6; i++) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date date = calendar.getTime();
			messagesSentLastWeek += dateService.getCount(customer.getESME(), mobileNumber, date);
		}
		calendar.setTime(currentDate);
		for(int i = 0; i < 29; i++) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date date = calendar.getTime();
			messagesSentLastMonth += dateService.getCount(customer.getESME(), mobileNumber, date);
		}
		
		int currentWeeklyCounter = customer.getWeeklyCap() - messagesSentLastWeek;
		int currentMonthlyCounter = customer.getMonthlyCap() - messagesSentLastMonth;
		
		setWeeklyCounter(customer, mobileNumber, currentWeeklyCounter);
		setMonthlyCounter(customer, mobileNumber, currentMonthlyCounter);
		resetDailyCounter(customer, mobileNumber);
		
	}
	public void AllCountersReset(Customer customer, String mobileNumber, Date currentDate) {
		if(dateService.isDatePresent(customer.getESME(), mobileNumber, currentDate)) {
			return;
		}
		
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(currentDate);
		calendar2.add(Calendar.DAY_OF_MONTH, -1);
		Date oneDayBeforeCurrentDate = calendar2.getTime();
		if(!dateService.isDatePresent(customer.getESME(), mobileNumber, oneDayBeforeCurrentDate)) {
			AllCountersResetLongerMethod(customer, mobileNumber, currentDate);
			return;
		}
		
		Date startDate = getStartDate(customer, mobileNumber);
		long daysPassed = (currentDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
		int currentWeeklyCounter = getWeeklyCounter(customer, mobileNumber);
		int currentMonthlyCounter = getMonthlyCounter(customer, mobileNumber);
		
		if(daysPassed >= 7) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.add(Calendar.DAY_OF_MONTH, -7);
			Date sevenDaysBeforeCurrentDate = calendar.getTime();
			setWeeklyCounter(customer, mobileNumber, dateService.getCount(customer.getESME(), mobileNumber, sevenDaysBeforeCurrentDate) + currentWeeklyCounter);
		}
		
		if(daysPassed >= 30) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.add(Calendar.DAY_OF_MONTH, -30);
			Date thirtyDaysBeforeCurrentDate = calendar.getTime();
			setMonthlyCounter(customer, mobileNumber, dateService.getCount(customer.getESME(), mobileNumber, thirtyDaysBeforeCurrentDate) + currentMonthlyCounter);
		}
		
	 
		resetDailyCounter(customer, mobileNumber);
		
	}
	
	public void resetDailyCounter(Customer customer, String mobileNumber) {
		setDailyCounter(customer, mobileNumber, customer.getDailyCap());
	}
	
	public void resetWeeklyCounter(Customer customer, String mobileNumber) {
		setWeeklyCounter(customer, mobileNumber, customer.getWeeklyCap());
	}
	
	public void resetMonthlyCounter(Customer customer, String mobileNumber) {
		setMonthlyCounter(customer, mobileNumber, customer.getMonthlyCap());
	}
	
	public boolean limitReached(Customer customer, String mobileNumber) {
		if(getDailyCounter(customer, mobileNumber) > 0 && getWeeklyCounter(customer, mobileNumber) > 0 && getMonthlyCounter(customer, mobileNumber) > 0) {
			return false;
		}
		
		return true;
	}
	
	public boolean limitReached(int dailyCounter, int weeklyCounter, int monthlyCounter) {
		return !(dailyCounter > 0 && weeklyCounter > 0 && monthlyCounter > 0);
	}
	
	public void decrementCounter(Customer customer, String mobileNumber) {
		int dailyCounter = getDailyCounter(customer, mobileNumber);
		int weeklyCounter = getWeeklyCounter(customer, mobileNumber);
		int monthlyCounter = getMonthlyCounter(customer, mobileNumber);
		dailyCounter--;
		weeklyCounter--;
		monthlyCounter--;
		setDailyCounter(customer, mobileNumber, dailyCounter);
		setWeeklyCounter(customer, mobileNumber, weeklyCounter);
		setMonthlyCounter(customer, mobileNumber, monthlyCounter);
	}
	
	public void printAllCounters(Customer customer, String mobileNumber) {
		System.out.println(getDailyCounter(customer, mobileNumber) + " " + getWeeklyCounter(customer, mobileNumber) + " " + getMonthlyCounter(customer, mobileNumber));
	}
	
	public boolean canSendMessage(Customer customer, String mobileNumber, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    date = calendar.getTime();
	    
		AllCountersReset(customer, mobileNumber, date);
		
		int dailyCounter = getDailyCounter(customer, mobileNumber);
		int weeklyCounter = getWeeklyCounter(customer, mobileNumber);
		int monthlyCounter = getMonthlyCounter(customer, mobileNumber);

		System.out.print(dailyCounter + " " + weeklyCounter + " " + monthlyCounter + " ==> ");
		if(limitReached(dailyCounter, weeklyCounter, monthlyCounter)) {
			System.out.println("MAX LIMIT OF MESSAGES REACHED ");
			if(!dateService.isDatePresent(customer.getESME(), mobileNumber, date)) {
				dateService.setCount(customer.getESME(), mobileNumber, date, 0);
			}
			return false;
		}else {
			decrementCounter(customer, mobileNumber);
			int currentDayCountOfMessages = dateService.getCount(customer.getESME(), mobileNumber, date);
			dateService.setCount(customer.getESME(), mobileNumber, date, currentDayCountOfMessages + 1);
			System.out.println("message sent");
			return true;
		}
		
	}
	
}
