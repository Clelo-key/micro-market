package com.micromarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.micromarket.entity.Order;
import com.micromarket.entity.OrderDetail;
import com.micromarket.entity.R;
import com.micromarket.properties.WeixinpayProperties;
import com.micromarket.service.IOrderDetailService;
import com.micromarket.service.IOrderService;
import com.micromarket.util.*;
import io.jsonwebtoken.Claims;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@RestController
@RequestMapping("/my/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private WeixinpayProperties weixinpayProperties;

    /**
     * 创建订单
     */
    @RequestMapping("/create")
    @Transactional
    public R create(@RequestHeader(value = "token") String token, @RequestBody Order order) {
        //使用token，获取openid
        Claims claims = JwtUtils.validateJWT(token).getClaims();
        if (claims != null) {
            order.setUserId(claims.getId());// 将openid设置到订单对象
        }
        order.setOrderNo("ORDER" + DateUtil.getCurrentDateStr());
        order.setCreateDate(new Date());
        OrderDetail[] goods = order.getGoods();
        orderService.save(order);
        for (OrderDetail item : goods) {
//            OrderDetail orderDetail=item;
            item.setMId(order.getId());
            orderDetailService.save(item);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderNo", order.getOrderNo());
        return R.ok(resultMap);
    }

    /**
     * 订单查询
     *
     * @param type 0=全部订单  1=待付款   2=待收货  3=退款/退货
     */
    @RequestMapping("/list")
    public R list(Integer type, Integer page, Integer pageSize) {
        System.out.println(type);
        Map<String, Object> resultMap = new HashMap<>();
        List<Order> orderList = null;
        Page<Order> pageOrder = new Page<>(page, pageSize);
        Page<Order> Pages = null;
        if (type == 0) {//全部查询
//            orderList=orderService.list(new QueryWrapper<Order>().orderByDesc("id"));
            Pages = orderService.page(pageOrder, new QueryWrapper<Order>().orderByDesc("id"));
            resultMap.put("total", Pages.getTotal());
//            resultMap.put("records",Pages.getRecords());
            resultMap.put("totalPage", Pages.getPages());
            orderList = Pages.getRecords();
            System.out.println(orderList);
        } else {
            Pages = orderService.page(pageOrder, new QueryWrapper<Order>().eq("status", type).orderByDesc("id"));
            resultMap.put("total", Pages.getTotal());
            resultMap.put("totalPage", Pages.getPages());
            orderList = Pages.getRecords();
            System.out.println(orderList);
        }
        resultMap.put("orderList", orderList);
        return R.ok(resultMap);

    }


    /**
     * 微信支付
     * 待未来添加功能。。。。。。。。。。。。。。。。。
     */
    @RequestMapping("/preparePay")
    public R preparePay(@RequestBody String orderNo) throws Exception {
        System.out.println(orderNo);
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("orderNo", orderNo));
        Map<String, Object> map = new HashMap<>();
        map.put("appid", weixinpayProperties.getAppid());
        map.put("mch_id", weixinpayProperties.getMch_id());
        map.put("noce_str", StringUtil.getRandomString(32));
        map.put("body", "商品购买测试");
        map.put("out_trade_no", orderNo);
        map.put("total_fee", order.getTotalPrice().movePointRight(2));
        map.put("spbill_create_ip", "127.0.0.1");
        map.put("trade_type", "JSAPI");
        map.put("notify_url", weixinpayProperties.getNotify_url());
        map.put("openid", order.getUserId());
        map.put("sign", getSign(map));
        String xml = XmlUtil.genXml(map);//生成xml
        HttpResponse httpResponse = HttpClientUtil.sendXMLDataByPost(weixinpayProperties.getUrl().toString(), xml);
        String httpEntityContent = HttpClientUtil.getHttpEntityContent(httpResponse);
        Map resultMap = XmlUtil.doXMLParse(httpEntityContent);
        if (resultMap.get("result_code").equals("SUCCESS")) {
            HashMap<String, Object> paymap = new HashMap<>();
            paymap.put("appId", resultMap.get("appid"));
            paymap.put("timeStemp", System.currentTimeMillis());
            paymap.put("nonCeStr", StringUtil.getRandomString(32));
            paymap.put("package", "prepay_id" + resultMap.get("prepay_id"));
            paymap.put("paySign", getSign(map));
            paymap.put("signType", "MD5");
            paymap.put("orderNO", orderNo);
            return R.ok(paymap);
        } else {
//            return R.error(500,"系统错误，请联系管理员");
            HashMap<String, Object> paymap = new HashMap<>();
            paymap.put("appId", resultMap.get("appid"));
            paymap.put("timeStemp", System.currentTimeMillis());
            paymap.put("nonCeStr", StringUtil.getRandomString(32));
            paymap.put("package", "prepay_id" + resultMap.get("prepay_id"));
            paymap.put("paySign", getSign(map));
            paymap.put("signType", "MD5");
            paymap.put("orderNO", orderNo);
            return R.ok(paymap);
        }
    }

    /**
     * 微信支付签名算法sign
     */
    private String getSign(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        String[] keyArr = (String[]) map.keySet().toArray(new String[map.keySet().size()]);//获取map中的key转为array
        Arrays.sort(keyArr);//对array排序
        for (int i = 0, size = keyArr.length; i < size; ++i) {
            if ("sign".equals(keyArr[i])) {
                continue;
            }
            sb.append(keyArr[i] + "=" + map.get(keyArr[i]) + "&");
        }
        sb.append("key=" + weixinpayProperties.getKey());
        String sign = string2MD5(sb.toString());
        System.out.println("sign=" + sign);
        return sign;
    }

    /**
     * MD5加密 生成32位md5码
     */
    private String string2MD5(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(str.getBytes(StandardCharsets.UTF_8));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }


}
