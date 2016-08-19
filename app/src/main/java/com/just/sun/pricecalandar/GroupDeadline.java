package com.just.sun.pricecalandar;

import java.io.Serializable;

/**
 * Created by pc-004 on 2015/9/17.
 */
public class GroupDeadline implements Serializable {
    /**
     * early_buy : 3
     */

    private String early_buy;
    /**
     * type_id : 1  1:国内游，2出境游3港澳台4周边游
     * orderId : first
     * type_tour_name : 国内游
     */

    private String type_id;
    private String orderId;
    private String type_tour_name;

    @Override
    public String toString() {
        return "GroupDeadline{" +
                "id='" + id + '\'' +
                ", tour_id='" + tour_id + '\'' +
                ", date='" + date + '\'' +
                ", stock='" + stock + '\'' +
                ", sell_price_adult='" + sell_price_adult + '\'' +
                ", trade_price_adult='" + trade_price_adult + '\'' +
                ", sell_price_elder='" + sell_price_elder + '\'' +
                ", trade_price_elder='" + trade_price_elder + '\'' +
                ", sell_price_children='" + sell_price_children + '\'' +
                ", trade_price_children='" + trade_price_children + '\'' +
                ", sell_price_children_bed='" + sell_price_children_bed + '\'' +
                ", trade_price_children_bed='" + trade_price_children_bed + '\'' +
                ", room_diff='" + room_diff + '\'' +
                ", is_discount='" + is_discount + '\'' +
                ", adult_profit=" + adult_profit +
                ", elder_profit=" + elder_profit +
                ", children_profit=" + children_profit +
                ", children_bed_profit=" + children_bed_profit +
                '}';
    }

    /**
     * id : 4
     * tour_id : 1
     * date : 2015-10-21
     * stock : 32
     * sell_price_adult : 432
     * trade_price_adult : 432
     * sell_price_elder : 231
     * trade_price_elder : 1231
     * sell_price_children : 3211
     * trade_price_children : 2323
     * sell_price_children_bed : 2233
     * trade_price_children_bed : 222
     * room_diff : 222
     * is_discount : 0
     * adult_profit : 0
     * elder_profit : -1000
     * children_profit : 888
     * children_bed_profit : 2011
     */
    //    private String id;
    //    private String tour_id;//产品ID
    //    private String date;
    //    private String stock;//团期数量
    //    private String sell_price_adult;//成人价格
    //    private String sell_price_elder;//老人价格
    //    private String sell_price_children;
    //    private String sell_price_children_bed;
    private String id;
    private String tour_id;//产品ID
    private String date;
    private String stock;//团期数量
    private String sell_price_adult;//成人价格
    private String trade_price_adult;
    private String sell_price_elder;
    private String trade_price_elder;
    private String sell_price_children;
    private String trade_price_children;
    private String sell_price_children_bed;
    private String trade_price_children_bed;
    private String room_diff;
    private String is_discount;
    private double adult_profit;//成人利润
    private double elder_profit;
    private double children_profit;
    private double children_bed_profit;


    public void setId(String id) {
        this.id = id;
    }

    public void setTour_id(String tour_id) {
        this.tour_id = tour_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setSell_price_adult(String sell_price_adult) {
        this.sell_price_adult = sell_price_adult;
    }

    public void setTrade_price_adult(String trade_price_adult) {
        this.trade_price_adult = trade_price_adult;
    }

    public void setSell_price_elder(String sell_price_elder) {
        this.sell_price_elder = sell_price_elder;
    }

    public void setTrade_price_elder(String trade_price_elder) {
        this.trade_price_elder = trade_price_elder;
    }

    public void setSell_price_children(String sell_price_children) {
        this.sell_price_children = sell_price_children;
    }

    public void setTrade_price_children(String trade_price_children) {
        this.trade_price_children = trade_price_children;
    }

    public void setSell_price_children_bed(String sell_price_children_bed) {
        this.sell_price_children_bed = sell_price_children_bed;
    }

    public void setTrade_price_children_bed(String trade_price_children_bed) {
        this.trade_price_children_bed = trade_price_children_bed;
    }

    public void setRoom_diff(String room_diff) {
        this.room_diff = room_diff;
    }

    public void setIs_discount(String is_discount) {
        this.is_discount = is_discount;
    }

    public void setAdult_profit(int adult_profit) {
        this.adult_profit = adult_profit;
    }

    public void setElder_profit(int elder_profit) {
        this.elder_profit = elder_profit;
    }

    public void setChildren_profit(int children_profit) {
        this.children_profit = children_profit;
    }

    public void setChildren_bed_profit(int children_bed_profit) {
        this.children_bed_profit = children_bed_profit;
    }

    public String getId() {
        return id;
    }

    public String getTour_id() {
        return tour_id;
    }

    public String getDate() {
        return date;
    }

    public String getStock() {
        return stock;
    }

    public String getSell_price_adult() {
        return sell_price_adult;
    }

    public String getTrade_price_adult() {
        return trade_price_adult;
    }

    public String getSell_price_elder() {
        return sell_price_elder;
    }

    public String getTrade_price_elder() {
        return trade_price_elder;
    }

    public String getSell_price_children() {
        return sell_price_children;
    }

    public String getTrade_price_children() {
        return trade_price_children;
    }

    public String getSell_price_children_bed() {
        return sell_price_children_bed;
    }

    public String getTrade_price_children_bed() {
        return trade_price_children_bed;
    }

    public String getRoom_diff() {
        return room_diff;
    }

    public String getIs_discount() {
        return is_discount;
    }

    public double getAdult_profit() {
        return adult_profit;
    }

    public double getElder_profit() {
        return elder_profit;
    }

    public double getChildren_profit() {
        return children_profit;
    }

    public double getChildren_bed_profit() {
        return children_bed_profit;
    }

    public void setEarly_buy(String early_buy) {
        this.early_buy = early_buy;
    }

    public String getEarly_buy() {
        return early_buy;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setType_tour_name(String type_tour_name) {
        this.type_tour_name = type_tour_name;
    }

    public String getType_id() {
        return type_id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getType_tour_name() {
        return type_tour_name;
    }


    //    private String date;
    //    private double price;
    //    private int state;//团期状态  0 过期 1 未过期
    //    private int quality;//团期数量


}
