package com.micromarket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micromarket.entity.OrderDetail;
import com.micromarket.mapper.OrderDetailMapper;
import com.micromarket.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("OrderDetailService")
public class IOrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
}
