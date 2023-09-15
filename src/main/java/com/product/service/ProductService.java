package com.product.service;

import com.product.entity.Product;
import com.product.entity.Response;

import java.util.List;

public interface ProductService {

    public Response addProduct(Product product);

    public Response addMultipleProducts(List<Product> products);

    public Response updateProduct(Product product);

    public Response getProductById(int id);

    public Response getProductByName(String name);

    public Response getProductsGreaterOrLessThanPrice(String order,double price);

    public Response getProductsBetweenPrice(double startingPrice, double endingPrice);

    public Response getAllProducts(int pageNo);

    public Response deleteProductById(int id);

}
