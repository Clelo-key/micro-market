package com.micromarket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micromarket.entity.Product;
import com.micromarket.mapper.ProductMapper;
import com.micromarket.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("productService")
public class IProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> list(Map<String, Object> map) {
        return productMapper.list(map);
    }
    @Override
    public Long getTotal(Map<String, Object> map) {
        return productMapper.getTotal(map);
    }

    @Override
    public void update(Product product) {
        productMapper.update(product);

    }

    @Override
    public void add(Product product) {
        productMapper.add(product);
    }

    @Override
    public Product findById(Integer id) {
        return productMapper.findById(id);
    }
}
