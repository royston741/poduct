package com.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator")
    @SequenceGenerator(
            name = "sequence-generator",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_desc")
    private String productDesc;

    @Column(name = "product_price")
    private Double productPrice;

}
