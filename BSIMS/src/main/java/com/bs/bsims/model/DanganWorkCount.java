package com.bs.bsims.model;

import java.io.Serializable;

public class DanganWorkCount implements Serializable {

	public DanganWorkCount getArray() {
		return array;
	}

	public void setArray(DanganWorkCount array) {
		this.array = array;
	}

	private String trains;
	private String transfer;
	private String userpay;
	private String interview;
	private String license;
	private DanganWorkCount rewards;
	
	
	private String absence;
	
	private String belate;
	
	private String nowrlog;
	
	private String askleave;
	
	private String customer;
	private String contract;
	private String payment;
	private String visitPerday;
	private String isSalesman;
	
	
	
	
	public String getIsSalesman() {
        return isSalesman;
    }

    public void setIsSalesman(String isSalesman) {
        this.isSalesman = isSalesman;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getVisitPerday() {
        return visitPerday;
    }

    public void setVisitPerday(String visitPerday) {
        this.visitPerday = visitPerday;
    }

    public String getAbsence() {
		return absence;
	}

	public void setAbsence(String absence) {
		this.absence = absence;
	}

	public String getBelate() {
		return belate;
	}

	public void setBelate(String belate) {
		this.belate = belate;
	}

	public String getNowrlog() {
		return nowrlog;
	}

	public void setNowrlog(String nowrlog) {
		this.nowrlog = nowrlog;
	}

	public String getAskleave() {
		return askleave;
	}

	public void setAskleave(String askleave) {
		this.askleave = askleave;
	}

	public DanganWorkCount getRewards() {
		return rewards;
	}

	public void setRewards(DanganWorkCount rewards) {
		this.rewards = rewards;
	}

	private DanganWorkCount array;
	private String code;
	private String one;
	private String two;

	public String getTrains() {
		return trains;
	}

	public String getOne() {
		return one;
	}

	public void setOne(String one) {
		this.one = one;
	}

	public String getTwo() {
		return two;
	}

	public void setTwo(String two) {
		this.two = two;
	}

	public void setTrains(String trains) {
		this.trains = trains;
	}

	public String getTransfer() {
		return transfer;
	}

	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}

	public String getUserpay() {
		return userpay;
	}

	public void setUserpay(String userpay) {
		this.userpay = userpay;
	}

	public String getInterview() {
		return interview;
	}

	public void setInterview(String interview) {
		this.interview = interview;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
