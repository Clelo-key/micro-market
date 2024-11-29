package com.micromarket.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micromarket.entity.PageBean;
import com.micromarket.entity.Product;
import com.micromarket.entity.R;
import com.micromarket.entity.SmallType;
import com.micromarket.service.IProductService;
import com.micromarket.service.ISmallTypeService;
import com.micromarket.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ISmallTypeService smallTypeService;

    @Value("${productImagesFilePath}")
    private String productImagesFilePath;
    @Value("${swiperImagesFilePath}")
    private String swiperImagesFilePath;

    /**
     * 根据条件分页查询
     *
     * @param pageBean
     * @return
     */
    @RequestMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", pageBean.getQuery().trim());
        map.put("start", pageBean.getStart());
        map.put("pageSize", pageBean.getPageSize());
        List<Product> list = productService.list(map);
        Long total = productService.getTotal(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("productList", list);
        resultMap.put("total", total);
        return R.ok(resultMap);
    }
    /**
     * 更新商品热卖操作
     * */
    @GetMapping("/updateHot/{id}/state/{hot}")
    public  R updateHot(@PathVariable(value = "id")Integer id,@PathVariable(value = "hot")boolean hot){
        Product product = productService.getById(id);
        product.setHot(hot);
        if (hot){
            product.setHotDateTime(new Date());
        }else {
            product.setHotDateTime(null);
        }
        productService.saveOrUpdate(product);
        return R.ok();
    }
    /**
     * 更新Swiper状态
     * */
    @GetMapping("/updateSwiper/{id}/state/{swiper}")
    public  R updateSwiper(@PathVariable(value = "id")Integer id,@PathVariable(value = "swiper")boolean swiper){
        Product product = productService.getById(id);
        product.setSwiper(swiper);
        productService.saveOrUpdate(product);
        return R.ok();
    }

    /**
     * 上传商品图片
     */
    @RequestMapping("/uploadImage")
    public Map<String, Object> uploadImage(MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (!file.isEmpty()) {
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            // 获取文件名的后缀名
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = DateUtil.getCurrentDateStr() + suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(productImagesFilePath + newFileName));
            resultMap.put("code", 0);
            resultMap.put("msg", "上传成功");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("title", newFileName);
            dataMap.put("src", "image/product/" + newFileName);
            resultMap.put("data", dataMap);
        }
        return resultMap;
    }

    /**
     * 添加或者修改
     */
    @RequestMapping("/save")
    public R save(@RequestBody Product product) {
        System.out.println(product.getHotDateTime());
        if (product.getId() == null || product.getId() == -1) {
            product.setId(null);
            productService.add(product);
        } else {
            productService.update(product);
        }
        return R.ok();
    }

    /**
     * 上传swiper幻灯图片
     */
    @RequestMapping("/uploadSwiperImage")
    public Map<String, Object> uploadSwiperImage(MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (!file.isEmpty()) {
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            // 获取文件名的后缀名
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = DateUtil.getCurrentDateStr() + suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(swiperImagesFilePath + newFileName));
            resultMap.put("code", 0);
            resultMap.put("msg", "上传成功");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("title", newFileName);
            dataMap.put("src", "image/swiper/" + newFileName);
            resultMap.put("data", dataMap);
        }
        return resultMap;
    }
    /**
     * 删除
     */
    @GetMapping("/delete/{id}")
    public R delete(@PathVariable(value="id") Integer id){
        productService.removeById(id);
        return R.ok();
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R findById(@PathVariable(value="id") Integer id){
        Product product = productService.findById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("product",product);
        return R.ok(map);
    }
    /**
     * 查询大类ID
     * @param id
     * @return
     */
    @RequestMapping("/getBigTypeIdBySmallTypeId/{id}")
    public R getBigTypeIdBySmallTypeId(@PathVariable(value = "id")Integer id){
        Integer bigTypeId=smallTypeService.getById(id).getBigTypeId();
        Map<String,Object> map=new HashMap<>();
        map.put("bigTypeId",bigTypeId);
        return R.ok(map);
    }

}