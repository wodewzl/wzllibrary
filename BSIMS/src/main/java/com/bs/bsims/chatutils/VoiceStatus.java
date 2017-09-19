package com.bs.bsims.chatutils;


/**
 * ����������λ�ú�״̬
 * @author zhuqian
 *
 */
public class VoiceStatus {

	private int pos = -1;
	private boolean status;
	public int getPos() {
		return pos;
	}
	public boolean isStatus() {
		return status;
	}
	
	public void put(int pos,boolean status){
		this.pos = pos;
		this.status = status;
	}
	
	public void clear(){
		this.pos = -1;
		this.status = false;
	}
	@Override
	public String toString() {
		return "VoiceStatus [pos=" + pos + ", status=" + status + "]";
	}
	
	
}
