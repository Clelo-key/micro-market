package com.micromarket.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品小类
 */
@TableName("t_smallType")
@Data
public class SmallType implements Serializable {

    private Integer id; // 编号

    private String name; // 名称

    private String remark; // 备注

    private Integer bigTypeId; // 大类id

    //    @TableField(select = false)
    @TableField(select = false, updateStrategy = FieldStrategy.NEVER, insertStrategy =
            FieldStrategy.NEVER)
    private BigType bigType;  // 所属商品大类

    @TableField(select = false)
    private List<Product> productList; // 商品集合


}
