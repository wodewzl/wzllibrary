package com.beisheng.easycar.mode;

import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.PayResult;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/6/1.
 */

public class PayTypeVO extends BaseVO{
    private PayTypeVO datas;
    private PayTypeVO data;
    private PayResult order;
    private List<PayTypeVO> list;
    private String payment_code;
    private String payment_name;

    private String bank_name;
    private String bank_code;
    private String bank_image;
    private boolean isCheck=false;


    private String card_id;
    private String member_id;
    private String true_name;
    private String mob_phone;
    private String card_no;
    private List<PayTypeVO> card_list;
    private String paypwd_state;
    private String set_paypwd_url;
    private String payOrder;


    public String getSet_paypwd_url() {
        return set_paypwd_url;
    }

    public void setSet_paypwd_url(String set_paypwd_url) {
        this.set_paypwd_url = set_paypwd_url;
    }

    public String getPaypwd_state() {
        return paypwd_state;
    }

    public void setPaypwd_state(String paypwd_state) {
        this.paypwd_state = paypwd_state;
    }

    public PayTypeVO getDatas() {
        return datas;
    }

    public void setDatas(PayTypeVO datas) {
        this.datas = datas;
    }

    public List<PayTypeVO> getList() {
        return list;
    }

    public void setList(List<PayTypeVO> list) {
        this.list = list;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getBank_image() {
        return bank_image;
    }

    public void setBank_image(String bank_image) {
        this.bank_image = bank_image;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }



    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getMob_phone() {
        return mob_phone;
    }

    public void setMob_phone(String mob_phone) {
        this.mob_phone = mob_phone;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public List<PayTypeVO> getCard_list() {
        return card_list;
    }

    public void setCard_list(List<PayTypeVO> card_list) {
        this.card_list = card_list;
    }

    public String getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(String payOrder) {
        this.payOrder = payOrder;
    }

    public PayTypeVO getData() {
        return data;
    }

    public void setData(PayTypeVO data) {
        this.data = data;
    }

    public PayResult getOrder() {
        return order;
    }

    public void setOrder(PayResult order) {
        this.order = order;
    }
}
