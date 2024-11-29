package com.micromarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micromarket.entity.BigType;
import com.micromarket.entity.Product;
import com.micromarket.entity.R;
import com.micromarket.entity.SmallType;
import com.micromarket.service.IProductService;
import com.micromarket.service.ISmallTypeService;
import com.micromarket.service.IBigTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品分类Controller
 * */
@RestController
@RequestMapping("/bigtype")
public class BigTypeController {
    @Autowired
    private IBigTypeService bigTypeService;
    @Autowired
    private ISmallTypeService smallTypeService;
    @Autowired
    private IProductService productService;



    /**
     *查询所有商品大类
     * */

    @GetMapping("/findAll")
    public R findAll(){
        List<BigType> bigTypeList = bigTypeService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("message",bigTypeList);
        return R.ok(map);
    }
    /**
     * 查询所有大类
     * */
    @GetMapping("/findCategories")
    public R findCategories(){
        //查询商品大类
        List<BigType> bigTypeList = bigTypeService.list();
        //查询商品小类
        for (BigType bigtype:bigTypeList) {
            List<SmallType> smallTypeList = smallTypeService.list(new QueryWrapper<SmallType>().eq("bigTypeId", bigtype.getId()));
            bigtype.setSmallTypeList(smallTypeList);
            for (SmallType smallType:smallTypeList
                 ) {
                List<Product> productList = productService.list(new QueryWrapper<Product>().eq("typeId", smallType.getId()));
                smallType.setProductList(productList);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("message",bigTypeList);
        return R.ok(map);
    }


}
