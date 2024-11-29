package com.micromarket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micromarket.entity.WxUserInfo;
import com.micromarket.mapper.WxUserInfoMapper;
import com.micromarket.service.IWxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("WxuserinfoService")
public class IWxUserInfoServiceImpl extends ServiceImpl<WxUserInfoMapper, WxUserInfo> implements IWxUserInfoService {
    @Autowired
    private WxUserInfoMapper WxUserInfoMapper;
}
