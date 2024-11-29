package com.micromarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.micromarket.entity.Product;
import com.micromarket.entity.ProductSwiperImage;
import com.micromarket.entity.R;
import com.micromarket.service.IProductService;
import com.micromarket.service.IProductSwiperImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 商品Controller
 */

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private IProductSwiperImageService productSwiperImageService;

    /**
     * 查询轮播图
     */

    @GetMapping("/findSwiper")
    public R findSwiper() {
        List<Product> swiperProductList = productService.list(new QueryWrapper<Product>().eq("isSwiper", true).orderByAsc("swiperSort"));
        Map<String, Object> map = new HashMap<>();
        map.put("message", swiperProductList);
        return R.ok(map);
    }

    /**
     * 查询热卖商品
     */
    @GetMapping("/findHot")
    public R findHot() {
        Page<Product> page = new Page<>(0, 8);
        Page<Product> pageProduct = productService.page(page, new QueryWrapper<Product>().eq("isHot", true).orderByAsc("hotDateTime"));
        List<Product> hotProductList = pageProduct.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("message", hotProductList);
        return R.ok(map);
    }


    /**
     * 查询商品详情
     */
    @GetMapping("/detail")
    public R detail(Integer id) {
        Product product = productService.getById(id);
        List<ProductSwiperImage> productSwiperImages = productSwiperImageService.list(new QueryWrapper<ProductSwiperImage>().eq("productId", product.getId()).orderByAsc("sort"));
        product.setProductSwiperImageList(productSwiperImages);
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", product);
        return R.ok(map);
    }
    /**
     * 搜索商品
     * */
    @RequestMapping("/search")
    public R search(String q){
        List<Product> products1 = productService.list(new QueryWrapper<Product>().like("name", q));
        List<Product> products2 = productService.list(new QueryWrapper<Product>().like("description",q));
        HashMap<String, Object> map = new HashMap<>();
        List<Product> collect = Stream.concat(products1.stream(), products2.stream()).collect(Collectors.toList());
        map.put("message", collect);
        return R.ok(map);
    }
}
