package com.micromarket.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micromarket.entity.Order;
import com.micromarket.entity.SmallType;
import com.micromarket.mapper.SmallTypeMapper;
import com.micromarket.service.ISmallTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("SmallService")
public class ISmallServiceImpl extends ServiceImpl<SmallTypeMapper, SmallType> implements ISmallTypeService {
    @Autowired
    private SmallTypeMapper SmallTypeMapper;

    @Override
    public List<Order> list(Map<String, Object> map) {
        return SmallTypeMapper.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return  SmallTypeMapper.getTotal(map);
    }
}
