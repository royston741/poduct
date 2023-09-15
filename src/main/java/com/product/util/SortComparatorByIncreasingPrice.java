package com.product.util;

import com.product.entity.Product;

import java.util.Comparator;

public class SortComparatorByIncreasingPrice implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Product product1 = (Product) o1;
        Product product2 = (Product) o2;
        if (product1.getProductPrice() > product2.getProductPrice()) {
            return 1;
        } else if (product1.getProductPrice() < product2.getProductPrice()) {
            return -1;
        } else {
            return 0;
        }
    }
}
