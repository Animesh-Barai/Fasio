package com.fashio.apps.Model;

import java.util.List;

public class Main_Order {

    private String order_id;
    private String tracking_id;
    private List<Order> orders;

    public Main_Order(String order_id, String res_margin, List<Order> orders,String tracking_id) {
        this.order_id = order_id;
        this.orders = orders;
        this.tracking_id = tracking_id;
    }

    public String getOrder_id() {
        return order_id;
    }


    public List<Order> getOrders() {
        return orders;
    }

    public String getTracking_id() {
        return tracking_id;
    }

}
