package com.product.serviceImpl;

import com.product.entity.Product;
import com.product.entity.Response;
import com.product.repository.ProductRepository;
import com.product.service.ProductService;
import com.product.util.SortComparatorByDecreasingPrice;
import com.product.util.SortComparatorByIncreasingPrice;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Response addProduct(Product product) {
        Response response = new Response(false, new ArrayList<>(), null);
        List<String> errMssgStringList = new ArrayList<>();
        try {
            if (product != null) {
                boolean productAlreadyExist = productRepository.checkIfProductExistByName(product.getProductName());
                if (productAlreadyExist) {
                    errMssgStringList.add("Product " + product.getProductName() + " already exist");
                } else {
                    if (checkProductValidity(product)) {
                        Product insertedProduct = productRepository.save(product);
                        response.setSuccess(true);
                        response.setResponseData(insertedProduct);
                    } else {
                        errMssgStringList.add("Inputs are not valid");
                    }
                }

            }
        } catch (Exception e) {
            errMssgStringList.add("Product not added");
            log.error("Error in addProduct {}", e);
        }
        response.setErrMessage(errMssgStringList);
        return response;
    }

    @Override
    public Response addMultipleProducts(List<Product> newProducts) {
        Response response = new Response(false, new ArrayList<>(), null);
        List<String> errMssgStringList = new ArrayList<>();
        try {
            if (newProducts.size() > 0) {
                List<Product> addedProducts = new ArrayList<>();
                List<String> notAddedProductsNameList = new ArrayList<>();
                newProducts.stream().forEach(p -> {
                    if (p != null) {
                        boolean productAlreadyExist = productRepository.checkIfProductExistByName(p.getProductName());
                        if (productAlreadyExist) {
                            notAddedProductsNameList.add(p.getProductName());
                        } else {
                            if (checkProductValidity(p)) {
                                Product insertedProduct = productRepository.save(p);
                                addedProducts.add(insertedProduct);
                            } else {
                                errMssgStringList.add("Inputs are not valid for " + p.getProductName());
                            }
                        }
                    }
                });
                if (notAddedProductsNameList.size() > 0) {
                    errMssgStringList.add(String.join(",", notAddedProductsNameList + " already exist"));
                }
                if (addedProducts.size() > 0) {
                    response.setResponseData(addedProducts);
                    response.setSuccess(true);
                }

            } else {
                errMssgStringList.add("No products to save");
            }
        } catch (Exception e) {
            log.error("Error in addMultipleProducts : {}", e);
        }
        response.setErrMessage(errMssgStringList);
        return response;
    }

    @Override
    @Transactional
    public Response updateProduct(Product product) {
        Response response = new Response(false, new ArrayList<>(), null);
        List<String> errMssgStringList = new ArrayList<>();
        try {
            if (product != null) {
                boolean productAlreadyExist = productRepository.existsById(product.getId());
                if (productAlreadyExist) {
                    if (checkProductValidity(product)) {
                        Product productToUpdate = productRepository.save(product);
                        response.setResponseData(productToUpdate);
                        response.setSuccess(true);
                    } else {
                        errMssgStringList.add("Inputs are not valid");
                    }
                } else {
                    errMssgStringList.add("Products does not exist with id " + product.getId());
                }

            } else {
                errMssgStringList.add("No product to update");
            }
        } catch (Exception e) {
            errMssgStringList.add("Product not updates");
            log.error("Error in updateProduct {}", e);
        }
        response.setErrMessage(errMssgStringList);
        return response;
    }

    @Override
    public Response getProductById(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        List<String> errMssgStringList = new ArrayList<>();
        try {
            Optional<Product> product = productRepository.findById(id);

            if (!product.isEmpty()) {
                response.setSuccess(true);
                response.setResponseData(product);
            } else {
                errMssgStringList.add("No product present by Id " + id);//
            }
        } catch (Exception e) {
            log.error("Error in getProductById {}", e);
        }
        response.setErrMessage(errMssgStringList);
        return response;
    }

    @Override
    public Response getProductByName(String name) {
        Response response = new Response(false, new ArrayList<>(), null);
        List<String> errMssgStringList = new ArrayList<>();
        try {
            Product product = productRepository.findByProductName(name);

            if (product != null) {
                response.setSuccess(true);
                response.setResponseData(product);
            } else {
                errMssgStringList.add("No product by name " + name);
            }
        } catch (Exception e) {
            log.error("Error in getProductById {}", e);
        }
        response.setErrMessage(errMssgStringList);
        return response;
    }

    @Override
    public Response getProductsGreaterOrLessThanPrice(String order, double price) {
        Response response = new Response(false, new ArrayList<>(), null);
        List<String> errMssgStringList = new ArrayList<>();
        try {
            List<Product> listOfProducts = new ArrayList<>();
            List<Product> allProducts = productRepository.findAll();

            if (order.equals("greater")) {
                listOfProducts = allProducts.stream().filter(product -> product.getProductPrice() > price).collect(Collectors.toList());
                Collections.sort(listOfProducts, (Object o1, Object o2) -> {
                    Product product1 = (Product) o1;
                    Product product2 = (Product) o2;
                    if (product1.getProductPrice() > product2.getProductPrice()) {
                        return 1;
                    } else if (product1.getProductPrice() < product2.getProductPrice()) {
                        return -1;
                    } else {
                        return 0;
                    }
                });

            } else if (order.equals("lesser")) {
                listOfProducts = allProducts.stream().filter(product -> product.getProductPrice() < price).collect(Collectors.toList());

                Collections.sort(listOfProducts, (Object o1, Object o2) -> {
                    Product product1 = (Product) o1;
                    Product product2 = (Product) o2;
                    if (product1.getProductPrice() < product2.getProductPrice()) {
                        return 1;
                    } else if (product1.getProductPrice() > product2.getProductPrice()) {
                        return -1;
                    } else {
                        return 0;
                    }
                });


            }
            if (listOfProducts.size() > 0) {
                response.setSuccess(true);
                response.setResponseData(listOfProducts);
            } else {
                errMssgStringList.add("No products");
            }
        } catch (Exception e) {
            log.error("Error in getProductsLessThanPrice {}", e);
        }
        response.setErrMessage(errMssgStringList);
        return response;
    }

    @Override
    public Response getProductsBetweenPrice(double startingPrice, double endingPrice) {
        Response response = new Response(false, new ArrayList<>(), null);
        List<String> errMssgStringList = new ArrayList<>();
        try {
            List<Product> allProducts = productRepository.findAll();
            List<Product> listOfProducts = allProducts.stream().filter(product -> product.getProductPrice() >= startingPrice && product.getProductPrice() <= endingPrice).collect(Collectors.toList());
            Collections.sort(listOfProducts, (Object o1, Object o2) -> {
                Product product1 = (Product) o1;
                Product product2 = (Product) o2;
                if (product1.getProductPrice() > product2.getProductPrice()) {
                    return 1;
                } else if (product1.getProductPrice() < product2.getProductPrice()) {
                    return -1;
                } else {
                    return 0;
                }
            });
            if (listOfProducts.size() > 0) {
                response.setSuccess(true);
                response.setResponseData(listOfProducts);
            } else {
                errMssgStringList.add("No products");
            }
        } catch (Exception e) {
            log.error("Error in getProductsGreaterThanPrice {}", e);
        }
        response.setErrMessage(errMssgStringList);
        return response;
    }

    @Override
    public Response getAllProducts(int pageNumber) {
        Response response = new Response(false, new ArrayList<>(), null);
        List<String> errMssgStringList = new ArrayList<>();
        Pageable page = PageRequest.of(pageNumber, 4);

//        List<Product> allProducts = productRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
        List<Product> allProducts = productRepository.findAll(page).stream().toList();

        try {
            if (allProducts.size() > 0) {
                response.setSuccess(true);
                response.setResponseData(allProducts);
            } else {
                errMssgStringList.add("No products");
            }
        } catch (Exception e) {
            errMssgStringList.add("No products");
            log.error("Error in getAllProducts {}", e);
        }
        response.setErrMessage(errMssgStringList);
        return response;
    }

    @Override
    public Response deleteProductById(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        List<String> errMssgStringList = new ArrayList<>();
        try {
            boolean productAlreadyExist = productRepository.existsById(id);
//            boolean isDeleted = products.removeIf(x -> x.getId() == id);
            if (productAlreadyExist) {
                productRepository.deleteById(id);
                response.setResponseData("Product is deleted");
                response.setSuccess(true);
            } else {
                errMssgStringList.add("Product with Id " + id + " not present");
            }
        } catch (Exception e) {
            log.error("Error in deleteProductById {}", e);
        }
        response.setErrMessage(errMssgStringList);
        return response;
    }

    public boolean checkProductValidity(Product product) {
        return (product.getProductName() != null
                && product.getProductDesc() != null && product.getProductPrice() != null
                && (!product.getProductName().matches("\\d+"))
                && (!product.getProductDesc().matches("\\d+")));
    }
}
