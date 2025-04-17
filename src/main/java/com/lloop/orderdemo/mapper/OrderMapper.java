package com.lloop.orderdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lloop.orderdemo.model.entity.UserOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author lloop
 * @Create 2025/4/18 00:05
 */
@Mapper
public interface OrderMapper extends BaseMapper<UserOrder> {

}
