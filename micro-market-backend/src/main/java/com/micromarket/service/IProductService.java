package com.micromarket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micromarket.entity.Product;

import java.util.List;
import java.util.Map;

public interface IProductService extends IService<Product> {
    List<Product> list(Map<String, Object> map);
    Long getTotal(Map<String, Object> map);

    void update(Product product);

    void add(Product product);

    Product findById(Integer id);
}
