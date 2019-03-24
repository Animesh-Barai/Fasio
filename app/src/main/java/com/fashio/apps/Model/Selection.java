package com.fashio.apps.Model;


import java.io.Serializable;
import java.util.List;


public  class Selection implements Serializable  {

    private String img_url;
    private Boolean select;
    private String img_id;
    private String price;
    private String actual_price;
    private String cat_name;
    private String count_all;
    private Boolean isLiked;
    private String image_name;
    private List<String> avalab_sizes;
    private String cod_avail;
    private String cat_desc;
    private String weight;
    private String img_rating;
    private String fsatisfy;

    public Selection(String img_url, Boolean select,String img_id,String price,String actual_price,String cat_name,String count_all,Boolean isLiked,String image_name,List<String> size_avail,String cod_avail,String cat_desc,String cat_weight,String img_rating,String fsatisfy) {
        this.img_url = img_url;
        this.select = select;
        this.img_id = img_id;
        this.price = price;
        this.actual_price = actual_price;
        this.cat_name = cat_name;
        this.isLiked = isLiked;
        this.count_all = count_all;
        this.image_name = image_name;
        this.avalab_sizes = size_avail;
        this.cod_avail = cod_avail;
        this.cat_desc = cat_desc;
        this.weight = cat_weight;
        this.img_rating=img_rating;
        this.fsatisfy = fsatisfy;
    }


    public Selection(String img_url, Boolean select,String img_id,String price,String actual_price,String cat_name,String count_all,Boolean isLiked,String image_name,List<String> size_avail,String cod_avail,String cat_desc,String cat_weight) {
        this.img_url = img_url;
        this.select = select;
        this.img_id = img_id;
        this.price = price;
        this.actual_price = actual_price;
        this.cat_name = cat_name;
        this.isLiked = isLiked;
        this.count_all = count_all;
        this.image_name = image_name;
        this.avalab_sizes = size_avail;
        this.cod_avail = cod_avail;
        this.cat_desc = cat_desc;
        this.weight = cat_weight;
    }

    public Selection(String img_url, Boolean select,String img_id,String price,String actual_price,String cat_name,String count_all,Boolean isLiked) {
        this.img_url = img_url;
        this.select = select;
        this.img_id = img_id;
        this.price = price;
        this.actual_price = actual_price;
        this.cat_name = cat_name;
        this.isLiked = isLiked;
        this.count_all = count_all;
    }

    public Selection(String img_url, Boolean select,String img_id) {
        this.img_url = img_url;
        this.select = select;
        this.img_id = img_id;
    }


    public String getImg_url() {
        return img_url;
    }

    public Boolean getSelect() {
        return select;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public String getPrice() {
        return price;
    }

    public String getActual_price() {
        return actual_price;
    }

    public String getCat_name() {
        return cat_name;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public String getCount_all() {
        return count_all;
    }

    public String getImage_name() {
        return image_name;
    }

    public String getCod_avail() {
        return cod_avail;
    }

    public List<String> getSize_avail() { return avalab_sizes;
    }

    public String getCat_desc() {
        return cat_desc;
    }

    public String getWeight() {
        return weight;
    }

    public String getImg_rating() {
        return img_rating;
    }

    public String getFsatisfy() {
        return fsatisfy;
    }
}
