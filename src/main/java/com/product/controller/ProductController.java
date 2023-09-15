package com.product.controller;

import com.product.entity.Product;
import com.product.entity.Response;
import com.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public ResponseEntity<Response> addProduct(@RequestBody Product product) {
        Response response = productService.addProduct(product);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addProducts")
    public ResponseEntity<Response> addMultipleProducts(@RequestBody List<Product> products) {
        Response response = productService.addMultipleProducts(products);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<Response> updateProduct(@RequestBody Product product) {
        Response response = productService.updateProduct(product);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable int id) {
        Response response = productService.getProductById(id);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getProductByName/{name}")
    public ResponseEntity<Response> getProductByName(@PathVariable String name) {
        Response response = productService.getProductByName(name);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getProductsGreaterOrLessThanPrice/{order}/{price}")
    public ResponseEntity<Response> getProductsGreaterOrLessThanPrice(@PathVariable String order,@PathVariable double price) {
        Response response = productService.getProductsGreaterOrLessThanPrice(order,price);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
    @GetMapping("/getProductsBetweenPrice/startPrice/{startingPrice}/endPrice/{endingPrice}")
    public ResponseEntity<Response> getProductsBetweenPrice(@PathVariable double startingPrice, @PathVariable double endingPrice) {
        Response response = productService.getProductsBetweenPrice(startingPrice, endingPrice);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<Response> getAllProducts(@RequestParam(name="page",defaultValue = "0") int pageNo) {
        Response response = productService.getAllProducts(pageNo);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Response> deleteProductById(@PathVariable int id) {
        Response response = productService.deleteProductById(id);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

}
