package com.micromarket.controller.admin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micromarket.entity.OrderDetail;
import com.micromarket.entity.R;
import com.micromarket.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/orderDetail")
public class AdminOrderDetailController {
    @Autowired
    private IOrderDetailService orderDetailService;
    /**
     * 根据订单号查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/list/{id}")
    public R listByOrderId(@PathVariable(value="id") Integer id){
        System.out.println("id="+id);
        List<OrderDetail> orderDetailList = orderDetailService.list(new
                QueryWrapper<OrderDetail>().eq("mId", id));
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("list",orderDetailList);
        return R.ok(resultMap);
    }
}
