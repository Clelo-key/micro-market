package com.micromarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micromarket.entity.Order;
import com.micromarket.entity.SmallType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmallTypeMapper extends BaseMapper<SmallType> {
    List<Order> list(Map<String, Object> map);
    Long getTotal(Map<String, Object> map);
    SmallType findById(Integer id);
}
