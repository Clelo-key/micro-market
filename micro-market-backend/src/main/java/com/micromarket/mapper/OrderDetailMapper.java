package com.micromarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micromarket.entity.BigType;
import com.micromarket.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
