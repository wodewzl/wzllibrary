package com.bs.bsims.model;

import java.io.Serializable;

/**
 * ITEM�Ķ�Ӧ���򻯶�������
 * */
public class ChannelItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/**
	 * ��Ŀ��ӦID
	 * */
	public Integer id;
	/**
	 * ��Ŀ��ӦNAME
	 * */
	public String name;

	/**
	 * ��Ŀ�Ƿ�ѡ��
	 * */
	public Integer selected;

	public ChannelItem() {
	}

	public ChannelItem(int id, String name, int selected) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.selected = Integer.valueOf(selected);
	}

	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public Integer getSelected() {
		return this.selected;
	}

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setSelected(Integer paramInteger) {
		this.selected = paramInteger;
	}

	public String toString() {
		return "ChannelItem [id=" + this.id + ", name=" + this.name + ", selected=" + this.selected + "]";
	}
}