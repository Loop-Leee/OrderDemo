package com.lloop.orderdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lloop.orderdemo.model.entity.UserOrder;

/**
 * @Author lloop
 * @Create 2025/4/17 16:32
 */
public interface OrderService extends IService<UserOrder> {
    String seckill(Long couponId, Long userId);
}