package com.the.marketapp.Model;

import android.content.Context;

public class CartCountModel {
Context context;

    public String getCartCount() {
        return cartCount;
    }

    public void setCartCount(String cartCount) {
        this.cartCount = cartCount;
    }

    String cartCount;

    public CartCountModel(String cartCount, String content) {

        this.cartCount = cartCount;

    }

}
