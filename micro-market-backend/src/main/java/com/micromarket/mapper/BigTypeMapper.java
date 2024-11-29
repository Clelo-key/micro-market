package com.micromarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.micromarket.entity.BigType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BigTypeMapper  extends BaseMapper<BigType> {
    BigType findById(Integer id);
}
