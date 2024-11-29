package com.micromarket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micromarket.entity.Admin;
import com.micromarket.mapper.AdminMapper;
import com.micromarket.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AdminService")
public class IAdminImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

}
