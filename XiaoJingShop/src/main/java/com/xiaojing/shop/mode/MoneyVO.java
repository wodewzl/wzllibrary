package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/5/5.
 */

public class MoneyVO extends BaseVO {
    private MoneyVO datas;
    private List<MoneyVO> list;
    private String lg_id;
    private String lg_member_id;
    private String lg_member_name;
    private String lg_type;
    private String lg_av_amount;
    private String lg_add_time;
    private String lg_desc;

    private String pl_id;
    private String pl_memberid;
    private String pl_membername;
    private String pl_stage;
    private String pl_beans;
    private String pl_addtime;
    private String stagetext;
    private MoneyVO bean_info;
    private MoneyVO gold_info;
    private String available_bean;
    private String freeze_bean;
    private String total_gold;
    private String total_bean;
    private String available_gold;
    private String freeze_gold;
    private MoneyVO predeposit_info;
    private String available_predeposit;
    private String have_card;
    private PayTypeVO card_info;
    private String paypwd_state;
    private String lg_balance;


    public MoneyVO getDatas() {
        return datas;
    }

    public void setDatas(MoneyVO datas) {
        this.datas = datas;
    }

    public List<MoneyVO> getList() {
        return list;
    }

    public void setList(List<MoneyVO> list) {
        this.list = list;
    }

    public String getLg_id() {
        return lg_id;
    }

    public void setLg_id(String lg_id) {
        this.lg_id = lg_id;
    }

    public String getLg_member_id() {
        return lg_member_id;
    }

    public void setLg_member_id(String lg_member_id) {
        this.lg_member_id = lg_member_id;
    }

    public String getLg_member_name() {
        return lg_member_name;
    }

    public void setLg_member_name(String lg_member_name) {
        this.lg_member_name = lg_member_name;
    }

    public String getLg_type() {
        return lg_type;
    }

    public void setLg_type(String lg_type) {
        this.lg_type = lg_type;
    }

    public String getLg_av_amount() {
        return lg_av_amount;
    }

    public void setLg_av_amount(String lg_av_amount) {
        this.lg_av_amount = lg_av_amount;
    }

    public String getLg_add_time() {
        return lg_add_time;
    }

    public void setLg_add_time(String lg_add_time) {
        this.lg_add_time = lg_add_time;
    }

    public String getLg_desc() {
        return lg_desc;
    }

    public void setLg_desc(String lg_desc) {
        this.lg_desc = lg_desc;
    }

    public String getPl_id() {
        return pl_id;
    }

    public void setPl_id(String pl_id) {
        this.pl_id = pl_id;
    }

    public String getPl_memberid() {
        return pl_memberid;
    }

    public void setPl_memberid(String pl_memberid) {
        this.pl_memberid = pl_memberid;
    }

    public String getPl_membername() {
        return pl_membername;
    }

    public void setPl_membername(String pl_membername) {
        this.pl_membername = pl_membername;
    }

    public String getPl_stage() {
        return pl_stage;
    }

    public void setPl_stage(String pl_stage) {
        this.pl_stage = pl_stage;
    }

    public String getPl_beans() {
        return pl_beans;
    }

    public void setPl_beans(String pl_beans) {
        this.pl_beans = pl_beans;
    }

    public String getPl_addtime() {
        return pl_addtime;
    }

    public void setPl_addtime(String pl_addtime) {
        this.pl_addtime = pl_addtime;
    }

    public String getStagetext() {
        return stagetext;
    }

    public void setStagetext(String stagetext) {
        this.stagetext = stagetext;
    }

    public MoneyVO getBean_info() {
        return bean_info;
    }

    public void setBean_info(MoneyVO bean_info) {
        this.bean_info = bean_info;
    }

    public String getAvailable_bean() {
        return available_bean;
    }

    public void setAvailable_bean(String available_bean) {
        this.available_bean = available_bean;
    }

    public String getFreeze_bean() {
        return freeze_bean;
    }

    public void setFreeze_bean(String freeze_bean) {
        this.freeze_bean = freeze_bean;
    }

    public String getTotal_gold() {
        return total_gold;
    }

    public void setTotal_gold(String total_gold) {
        this.total_gold = total_gold;
    }

    public String getTotal_bean() {
        return total_bean;
    }

    public void setTotal_bean(String total_bean) {
        this.total_bean = total_bean;
    }

    public String getAvailable_gold() {
        return available_gold;
    }

    public void setAvailable_gold(String available_gold) {
        this.available_gold = available_gold;
    }

    public String getFreeze_gold() {
        return freeze_gold;
    }

    public void setFreeze_gold(String freeze_gold) {
        this.freeze_gold = freeze_gold;
    }

    public MoneyVO getGold_info() {
        return gold_info;
    }

    public void setGold_info(MoneyVO gold_info) {
        this.gold_info = gold_info;
    }

    public MoneyVO getPredeposit_info() {
        return predeposit_info;
    }

    public void setPredeposit_info(MoneyVO predeposit_info) {
        this.predeposit_info = predeposit_info;
    }

    public String getAvailable_predeposit() {
        return available_predeposit;
    }

    public void setAvailable_predeposit(String available_predeposit) {
        this.available_predeposit = available_predeposit;
    }

    public String getHave_card() {
        return have_card;
    }

    public void setHave_card(String have_card) {
        this.have_card = have_card;
    }

    public PayTypeVO getCard_info() {
        return card_info;
    }

    public void setCard_info(PayTypeVO card_info) {
        this.card_info = card_info;
    }

    public String getPaypwd_state() {
        return paypwd_state;
    }

    public void setPaypwd_state(String paypwd_state) {
        this.paypwd_state = paypwd_state;
    }

    public String getLg_balance() {
        return lg_balance;
    }

    public void setLg_balance(String lg_balance) {
        this.lg_balance = lg_balance;
    }
}
