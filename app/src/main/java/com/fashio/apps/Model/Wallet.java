package com.fashio.apps.Model;


public class Wallet {

    private String trans_id;
    private String trans_type;
    private String trans_amount;
    private String trans_status;

    public Wallet(String trans_id, String trans_type, String trans_amount, String trans_status) {
        this.trans_id = trans_id;
        this.trans_type = trans_type;
        this.trans_amount = trans_amount;
        this.trans_status = trans_status;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public String getTrans_amount() {
        return trans_amount;
    }

    public String getTrans_status() {
        return trans_status;
    }
}
