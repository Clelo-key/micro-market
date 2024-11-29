package com.micromarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micromarket.entity.Order;
import com.micromarket.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    List<Product> list(Map<String, Object> map);
    Long getTotal(Map<String, Object> map);

    void add(Product product);

    void update(Product product);

    Product findById(Integer id);
}
