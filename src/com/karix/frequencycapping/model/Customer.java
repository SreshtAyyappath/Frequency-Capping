package com.karix.frequencycapping.model;

import com.karix.frequencycapping.dao.CustomerRedis;

public class Customer {
	String ESME;
	CustomerRedis customerRedis ;
	
//	int dailyCap;
//	int weeklyCap;
//	int monthlyCap;
	
	public Customer(String ESME, int dailyCap, int weeklyCap, int monthlyCap){
		customerRedis = new CustomerRedis();
		this.ESME = ESME;
		setDailyCap(dailyCap);
		setWeeklyCap(weeklyCap);
		setMonthlyCap(monthlyCap);
	}
	
	public String getESME() {
		return ESME;
	}

	public void setESME(String customerName) {
		this.ESME = customerName;
	}

	public int getDailyCap() {
		return customerRedis.getDailyCap(ESME);
	}

	public void setDailyCap(int dailyCap) {
		customerRedis.setDailyCap(ESME, dailyCap);
	}

	public int getWeeklyCap() {
		return customerRedis.getWeeklyCap(ESME);
	}

	public void setWeeklyCap(int weeklyCap) {
		customerRedis.setWeeklyCap(ESME, weeklyCap);
	}

	public int getMonthlyCap() {
		return customerRedis.getMonthlyCap(ESME);
	}

	public void setMonthlyCap(int monthlyCap) {
		customerRedis.setMonthlyCap(ESME, monthlyCap);
	}
	
	
}
