package com.micromarket.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.micromarket.Constant.SystemConstant;
import com.micromarket.entity.Admin;
import com.micromarket.entity.R;
import com.micromarket.service.IAdminService;
import com.micromarket.util.JwtUtils;
import com.micromarket.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 管理员controller
 * */
@RestController
public class AdminController {
    @Autowired
    private IAdminService iAdminService;
    /***
     * 用户登录功能
     * */
    @PostMapping("/adminLogin")
    public R adminLogin(@RequestBody Admin admin){
        if (admin==null){
            return R.error();
        }
        if (StringUtil.isEmpty(admin.getUsername())){
            return R.error("用户名不能为空");
        }
        if (StringUtil.isEmpty(admin.getPassword())){
            return R.error("密码不能为空");
        }
        Admin user = iAdminService.getOne(new QueryWrapper<Admin>().eq("username", admin.getUsername()));
        if (user==null){
            return R.error("用户名或密码错误");
        }
        else if (!Objects.equals(user.getPassword(), admin.getPassword())){
            return R.error("用户名和密码不匹配");
        }
        String token = JwtUtils.createJWT("-1", "admin", SystemConstant.JWT_TTL);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("token",token);
        resultMap.put("user",admin);
        return R.ok(resultMap);
    }
    @PostMapping("/admin/modifyPassword")
    public R modifyPassword(@RequestBody Admin admin){
        if (StringUtil.isEmpty(admin.getUsername())){
            return R.error("用户名不能为空");
        }
        if (StringUtil.isEmpty(admin.getNewPassword())){
            return R.error("密码不能为空");
        }
        iAdminService.update(new UpdateWrapper<Admin>().set("password",admin.getNewPassword()).eq("username",admin.getUsername()));
        return R.ok();
    }


}
