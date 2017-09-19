package com.wzl.shishicai;

import java.io.Serializable;
import java.util.List;

public class DataVO implements Serializable {
    public String id;
    public String result;
    private String bigSmall;
    private String danShuang;
    private String yuce3;
    private String yuce4;
    private String yuce5;
    private String yuce6;
    private List<DataVO> list;
    private String number3;
    private String number4;
    private String number5;
    private String mark;
    private String quyu;
    private String number4Result;
    private String numberResult;
    private String number3Result;
    private String maxYiLou;
    private String repeat="";

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBigSmall() {
        return bigSmall;
    }

    public void setBigSmall(String bigSmall) {
        this.bigSmall = bigSmall;
    }

    public String getDanShuang() {
        return danShuang;
    }

    public void setDanShuang(String danShuang) {
        this.danShuang = danShuang;
    }

    public String getYuce3() {
        return yuce3;
    }

    public void setYuce3(String yuce3) {
        this.yuce3 = yuce3;
    }

    public String getYuce4() {
        return yuce4;
    }

    public void setYuce4(String yuce4) {
        this.yuce4 = yuce4;
    }

    public String getYuce5() {
        return yuce5;
    }

    public void setYuce5(String yuce5) {
        this.yuce5 = yuce5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DataVO> getList() {
        return list;
    }

    public void setList(List<DataVO> list) {
        this.list = list;
    }

    public String getYuce6() {
        return yuce6;
    }

    public void setYuce6(String yuce6) {
        this.yuce6 = yuce6;
    }

    public String getNumber4() {
        return number4;
    }

    public void setNumber4(String number4) {
        this.number4 = number4;
    }

    public String getNumber5() {
        return number5;
    }

    public void setNumber5(String number5) {
        this.number5 = number5;
    }



    public String getNumber3() {
        return number3;
    }

    public void setNumber3(String number3) {
        this.number3 = number3;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getQuyu() {
        return quyu;
    }

    public void setQuyu(String quyu) {
        this.quyu = quyu;
    }

    public String getNumber4Result() {
        return number4Result;
    }

    public void setNumber4Result(String number4Result) {
        this.number4Result = number4Result;
    }

    public String getNumber3Result() {
        return number3Result;
    }

    public void setNumber3Result(String number3Result) {
        this.number3Result = number3Result;
    }

    public String getNumberResult() {
        return numberResult;
    }

    public void setNumberResult(String numberResult) {
        this.numberResult = numberResult;
    }

    public String getMaxYiLou() {
        return maxYiLou;
    }

    public void setMaxYiLou(String maxYiLou) {
        this.maxYiLou = maxYiLou;
    }

    public String getRepeat() {

        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
