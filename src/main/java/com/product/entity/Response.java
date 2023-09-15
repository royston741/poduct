package com.product.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private boolean isSuccess;
    private List<String> errMessage=new ArrayList<>();
    private Object responseData;
}
