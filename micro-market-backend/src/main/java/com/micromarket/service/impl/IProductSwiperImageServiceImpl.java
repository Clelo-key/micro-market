package com.micromarket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micromarket.entity.ProductSwiperImage;
import com.micromarket.mapper.ProductSwiperImageMapper;
import com.micromarket.service.IProductSwiperImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ProductSwiperImageService")
public class IProductSwiperImageServiceImpl extends ServiceImpl<ProductSwiperImageMapper, ProductSwiperImage> implements IProductSwiperImageService {
    @Autowired
    ProductSwiperImageMapper productSwiperImageMapper;
}
