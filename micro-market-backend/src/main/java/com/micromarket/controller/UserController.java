package com.micromarket.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micromarket.Constant.SystemConstant;
import com.micromarket.entity.R;
import com.micromarket.entity.WxUserInfo;
import com.micromarket.properties.WeixinProperties;
import com.micromarket.service.IWxUserInfoService;
import com.micromarket.util.HttpClientUtil;
import com.micromarket.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信用户Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private WeixinProperties weixinProperties;
    @Autowired
    private HttpClientUtil httpClientUtils;
    @Autowired
    private IWxUserInfoService wxUserInfoService;

    /**
     * 微信用户登录
     */
    @RequestMapping("/wxlogin")
    public R WxLogin(@RequestBody WxUserInfo wxUserInfo) {
        System.out.println(wxUserInfo.getCode());
        String jscode2sessionUrl = weixinProperties.getJscode2sessionUrl() + "?appid=" + weixinProperties.getAppid() + "&secret=" + weixinProperties.getSecret() + "&js_code=" + wxUserInfo.getCode() + "&grant_type=authorization_code";
        System.out.println(jscode2sessionUrl);
        String result = httpClientUtils.sendHttpGet(jscode2sessionUrl);
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.get("openid").toString();
        WxUserInfo resultWxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("openid", openid));
        if (resultWxUserInfo == null) {//如果存在用户
            wxUserInfo.setOpenid(openid);
            wxUserInfo.setRegisterDate(new Date());
            wxUserInfo.setLastLoginDate(new Date());
            wxUserInfoService.save(wxUserInfo);
        } else {//如果不存在用户
            resultWxUserInfo.setOpenid(openid);
            resultWxUserInfo.setNickName(wxUserInfo.getNickName());
            resultWxUserInfo.setAvatarUrl(wxUserInfo.getAvatarUrl());
            resultWxUserInfo.setLastLoginDate(new Date());
            wxUserInfoService.updateById(resultWxUserInfo);
        }
        String token = JwtUtils.createJWT(openid, wxUserInfo.getNickName(), SystemConstant.JWT_TTL);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", token);
        System.out.println(resultMap.get("token"));
        return R.ok(resultMap);

    }


}
