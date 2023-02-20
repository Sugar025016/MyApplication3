package com.example.doll.entity;



/**
 * @author: CartResponse
 * @date: 2021/10/20
 * @description:
 */

public class CartResponse {

    private Integer id;

    private Integer productId;

    private String title;

    private int qty;

    private int total;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    private int totalOriginPrice;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalOriginPrice() {
        return totalOriginPrice;
    }

    public void setTotalOriginPrice(int totalOriginPrice) {
        this.totalOriginPrice = totalOriginPrice;
    }
}
