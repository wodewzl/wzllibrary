package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/5/12.
 */

public class OrderVO extends BaseVO {
    private OrderVO datas;
    private List<OrderVO> order_list;
    private String order_id;
    private String order_sn;
    private String pay_sn;
    private String store_name;
    private String order_amount;
    private String shipping_fee;
    private String order_state;
    private String evaluation_state;
    private String lock_state;
    private String state_desc;
    private String if_cancel;
    private String if_payment;
    private String if_receive;
    private String if_lock;
    private String if_deliver;
    private String if_delete;
    private String zengpin_list;
    private String pay_amount;
    private List<OrderVO> extend_order_goods;
    private String rec_id;
    private String goods_id;
    private String goods_name;
    private String goods_price;
    private String goods_num;
    private String goods_image;
    private String goods_pay_price;
    private String store_id;
    private String buyer_id;
    private String goods_type;
    private String promotions_id;
    private String commis_rate;
    private String gc_id;
    private String goods_image_url;
    private boolean isOrder = false;
    private String goods_count;
    private String freight_hash;
    private OrderVO address_info;
    private String address_id;
    private String address;
    private String true_name;
    private String mob_phone;
    private String available_predeposit;
    private String available_gold;
    private String gold_desc;
    private String available_bean;
    private String bean_desc;
    private List<OrderVO>  payment_list;
    private String payment_code;
    private String payment_name;
    private String predeposit_desc;
    private String available_predeposit_amount;
    private String available_gold_amount;
    private String available_bean_amount;
    private String detail_url;
    private String if_evaluation;
    private String evaluation_url;
    private String if_refund_cancel;
    private String refund_cancel_url;
    private List<OrderVO> return_list;
    private List<OrderVO> goods_list;
    private String goods_img;
    private String refund_amount;
    private String add_time;
    private String ship_state;
    private String delay_state;
    private String ship_url;
    private String delay_confirm_text;
    private String refund_id;
    private String paypwd_state;
    private String set_paypwd_url;


    public OrderVO getDatas() {
        return datas;
    }

    public void setDatas(OrderVO datas) {
        this.datas = datas;
    }

    public List<OrderVO> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(List<OrderVO> order_list) {
        this.order_list = order_list;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getEvaluation_state() {
        return evaluation_state;
    }

    public void setEvaluation_state(String evaluation_state) {
        this.evaluation_state = evaluation_state;
    }

    public String getLock_state() {
        return lock_state;
    }

    public void setLock_state(String lock_state) {
        this.lock_state = lock_state;
    }

    public String getState_desc() {
        return state_desc;
    }

    public void setState_desc(String state_desc) {
        this.state_desc = state_desc;
    }

    public String getIf_cancel() {
        return if_cancel;
    }

    public void setIf_cancel(String if_cancel) {
        this.if_cancel = if_cancel;
    }

    public String getIf_payment() {
        return if_payment;
    }

    public void setIf_payment(String if_payment) {
        this.if_payment = if_payment;
    }

    public String getIf_receive() {
        return if_receive;
    }

    public void setIf_receive(String if_receive) {
        this.if_receive = if_receive;
    }

    public String getIf_lock() {
        return if_lock;
    }

    public void setIf_lock(String if_lock) {
        this.if_lock = if_lock;
    }

    public String getIf_deliver() {
        return if_deliver;
    }

    public void setIf_deliver(String if_deliver) {
        this.if_deliver = if_deliver;
    }

    public String getIf_delete() {
        return if_delete;
    }

    public void setIf_delete(String if_delete) {
        this.if_delete = if_delete;
    }

    public String getZengpin_list() {
        return zengpin_list;
    }

    public void setZengpin_list(String zengpin_list) {
        this.zengpin_list = zengpin_list;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public List<OrderVO> getExtend_order_goods() {
        return extend_order_goods;
    }

    public void setExtend_order_goods(List<OrderVO> extend_order_goods) {
        this.extend_order_goods = extend_order_goods;
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getGoods_pay_price() {
        return goods_pay_price;
    }

    public void setGoods_pay_price(String goods_pay_price) {
        this.goods_pay_price = goods_pay_price;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getPromotions_id() {
        return promotions_id;
    }

    public void setPromotions_id(String promotions_id) {
        this.promotions_id = promotions_id;
    }

    public String getCommis_rate() {
        return commis_rate;
    }

    public void setCommis_rate(String commis_rate) {
        this.commis_rate = commis_rate;
    }

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }

    public String getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(String goods_count) {
        this.goods_count = goods_count;
    }

    public String getFreight_hash() {
        return freight_hash;
    }

    public void setFreight_hash(String freight_hash) {
        this.freight_hash = freight_hash;
    }

    public OrderVO getAddress_info() {
        return address_info;
    }

    public void setAddress_info(OrderVO address_info) {
        this.address_info = address_info;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailable_predeposit() {
        return available_predeposit;
    }

    public void setAvailable_predeposit(String available_predeposit) {
        this.available_predeposit = available_predeposit;
    }

    public String getAvailable_gold() {
        return available_gold;
    }

    public void setAvailable_gold(String available_gold) {
        this.available_gold = available_gold;
    }

    public String getGold_desc() {
        return gold_desc;
    }

    public void setGold_desc(String gold_desc) {
        this.gold_desc = gold_desc;
    }

    public String getAvailable_bean() {
        return available_bean;
    }

    public void setAvailable_bean(String available_bean) {
        this.available_bean = available_bean;
    }

    public String getBean_desc() {
        return bean_desc;
    }

    public void setBean_desc(String bean_desc) {
        this.bean_desc = bean_desc;
    }

    public List<OrderVO> getPayment_list() {
        return payment_list;
    }

    public void setPayment_list(List<OrderVO> payment_list) {
        this.payment_list = payment_list;
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

    public String getPredeposit_desc() {
        return predeposit_desc;
    }

    public void setPredeposit_desc(String predeposit_desc) {
        this.predeposit_desc = predeposit_desc;
    }

    public String getAvailable_predeposit_amount() {
        return available_predeposit_amount;
    }

    public void setAvailable_predeposit_amount(String available_predeposit_amount) {
        this.available_predeposit_amount = available_predeposit_amount;
    }

    public String getAvailable_gold_amount() {
        return available_gold_amount;
    }

    public void setAvailable_gold_amount(String available_gold_amount) {
        this.available_gold_amount = available_gold_amount;
    }

    public String getAvailable_bean_amount() {
        return available_bean_amount;
    }

    public void setAvailable_bean_amount(String available_bean_amount) {
        this.available_bean_amount = available_bean_amount;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getIf_evaluation() {
        return if_evaluation;
    }

    public void setIf_evaluation(String if_evaluation) {
        this.if_evaluation = if_evaluation;
    }

    public String getEvaluation_url() {
        return evaluation_url;
    }

    public void setEvaluation_url(String evaluation_url) {
        this.evaluation_url = evaluation_url;
    }

    public String getIf_refund_cancel() {
        return if_refund_cancel;
    }

    public void setIf_refund_cancel(String if_refund_cancel) {
        this.if_refund_cancel = if_refund_cancel;
    }

    public String getRefund_cancel_url() {
        return refund_cancel_url;
    }

    public void setRefund_cancel_url(String refund_cancel_url) {
        this.refund_cancel_url = refund_cancel_url;
    }

    public List<OrderVO> getReturn_list() {
        return return_list;
    }

    public void setReturn_list(List<OrderVO> return_list) {
        this.return_list = return_list;
    }

    public List<OrderVO> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<OrderVO> goods_list) {
        this.goods_list = goods_list;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getShip_state() {
        return ship_state;
    }

    public void setShip_state(String ship_state) {
        this.ship_state = ship_state;
    }

    public String getDelay_state() {
        return delay_state;
    }

    public void setDelay_state(String delay_state) {
        this.delay_state = delay_state;
    }

    public String getShip_url() {
        return ship_url;
    }

    public void setShip_url(String ship_url) {
        this.ship_url = ship_url;
    }

    public String getDelay_confirm_text() {
        return delay_confirm_text;
    }

    public void setDelay_confirm_text(String delay_confirm_text) {
        this.delay_confirm_text = delay_confirm_text;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getPaypwd_state() {
        return paypwd_state;
    }

    public void setPaypwd_state(String paypwd_state) {
        this.paypwd_state = paypwd_state;
    }

    public String getSet_paypwd_url() {
        return set_paypwd_url;
    }

    public void setSet_paypwd_url(String set_paypwd_url) {
        this.set_paypwd_url = set_paypwd_url;
    }
}
