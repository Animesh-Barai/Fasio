package com.fashio.apps.Model;


import java.io.Serializable;

public class Cart implements Serializable {


    private String book_id;
    private String bookName;
    private String imgUrl;
    private byte[] image;
    private String bookPrice;
    private String quantity;
    private long row_id;
    private String mrp;
    private String avail_size;
    private String product_weight;

    public Cart(String book_id, String bookName, String bookPrice, String quantity, String imgUrl, String avail_size, String product_weight) {
        this.book_id = book_id;
        this.bookName = bookName;
        this.imgUrl = imgUrl;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
        this.avail_size = avail_size;
        this.product_weight=product_weight;
    }

    public Cart(String book_id, String bookName, byte[] image, String bookPrice, String quantity, String imgUrl) {
        this.book_id = book_id;
        this.bookName = bookName;
        this.image = image;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
        this.mrp = mrp;
    }



    public Cart() {
        //Empty Constructor
    }

    public Cart(String bookName, String quantity, byte[] image, String bookPrice) {
        this.bookName = bookName;
        this.image = image;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
    }

    public Cart(String book_id, String product_name, String price, String qty) {
        this.bookName = product_name;
        this.book_id = book_id;
        this.bookPrice = price;
        this.quantity = qty;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getBookName() {
        return bookName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public byte[] getImage() {
        return image;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public long getRow_id() {
        return row_id;
    }

    public void setRow_id(long row_id) {
        this.row_id = row_id;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getAvail_size() {
        return avail_size;
    }

    @Override
    public String toString() {
        return "Cart [book_id=" + book_id + ","
                + " book_name=" + bookName + ","
                + " img_url=" + imgUrl
                + " book_price=" + bookPrice + ","
                + " quantity=" + quantity +"]";
    }

}

