package com.micromarket.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micromarket.entity.WxUserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxUserInfoMapper extends BaseMapper<WxUserInfo> {
    WxUserInfo findOpenId(String openid);

}
