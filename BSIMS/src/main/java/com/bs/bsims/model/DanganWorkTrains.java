package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class DanganWorkTrains implements Serializable {

	private String trainWork;
	private String udscore;
	private String udabsence;
	private String udlate;
	private String udlatetime;
	private String trainTitle;
	private String trainStartime;
	private List<DanganWorkTrains> array;
	private String count;
	private String code;
	public List<DanganWorkTrains> getArray() {
		return array;
	}
	public void setArray(List<DanganWorkTrains> array) {
		this.array = array;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTrainWork() {
		return trainWork;
	}
	public void setTrainWork(String trainWork) {
		this.trainWork = trainWork;
	}
	public String getUdscore() {
		return udscore;
	}
	public void setUdscore(String udscore) {
		this.udscore = udscore;
	}
	public String getUdabsence() {
		return udabsence;
	}
	public void setUdabsence(String udabsence) {
		this.udabsence = udabsence;
	}
	public String getUdlate() {
		return udlate;
	}
	public void setUdlate(String udlate) {
		this.udlate = udlate;
	}
	public String getUdlatetime() {
		return udlatetime;
	}
	public void setUdlatetime(String udlatetime) {
		this.udlatetime = udlatetime;
	}
	public String getTrainTitle() {
		return trainTitle;
	}
	public void setTrainTitle(String trainTitle) {
		this.trainTitle = trainTitle;
	}
	public String getTrainStartime() {
		return trainStartime;
	}
	public void setTrainStartime(String trainStartime) {
		this.trainStartime = trainStartime;
	}
	
}
