package com.micromarket.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micromarket.entity.Order;
import com.micromarket.entity.OrderDetail;
import com.micromarket.entity.PageBean;
import com.micromarket.entity.R;
import com.micromarket.service.IOrderDetailService;
import com.micromarket.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDetailService orderDetailService;

    /**
     * 根据条件分页查询
     *
     * @param pageBean
     * @return
     */
    @RequestMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        System.out.println(pageBean);
        Map<String, Object> map = new HashMap<>();
        map.put("name", pageBean.getQuery().trim());
        map.put("start", pageBean.getStart());
        map.put("pageSize", pageBean.getPageSize());
        List<Order> list = orderService.list(map);
        Long total = orderService.getTotal(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderList", list);
        resultMap.put("total", total);
        return R.ok(resultMap);
    }

    @PostMapping("/updateStatus")
    public R updateStatus(@RequestBody Order order) {
        Order resultOrder = orderService.getById(order.getId());
        resultOrder.setStatus(order.getStatus());
        orderService.saveOrUpdate(resultOrder);
        return R.ok();
    }

    @GetMapping("/delete/{id}")
    public R delete(@PathVariable(value = "id") Integer id) {
        // 删除订单细表数据
        orderDetailService.remove(new QueryWrapper<OrderDetail>().eq("mId", id));
        orderService.removeById(id);
        return R.ok();
    }
}