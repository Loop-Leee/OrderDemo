package com.lloop.orderdemo.service.impl;

import com.lloop.orderdemo.service.OrderService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String STOCK_KEY_PREFIX = "coupon:stock:";
    private static final String USER_KEY_PREFIX = "coupon:users:";
    private static final String LOCK_KEY_PREFIX = "lock:coupon:";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String seckill(Long couponId, Long userId) {
        String lockKey = LOCK_KEY_PREFIX + couponId + ":" + userId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
		        // 尝试上锁，指定没抢到的3秒内等待锁，不设置锁的占用时间（默认启动看门狗）
		        boolean locked = lock.tryLock(3, TimeUnit.SECONDS);
            if (!locked) {
                return "系统繁忙，请稍后再试";
	          }

            // 1. 判断用户是否已经抢购过
            Boolean hasBought = redisTemplate.opsForSet().isMember(USER_KEY_PREFIX + couponId, userId.toString());
            if (Boolean.TRUE.equals(hasBought)) {
                return "请勿重复抢购";
            }

            // 2. 判断库存是否足够
            String stockKey = STOCK_KEY_PREFIX + couponId;
            Long stock = redisTemplate.opsForValue().decrement(stockKey);
            if (stock < 0) {
                return "抱歉，库存不足";
            }

            // 3. 抢购成功，记录用户
            redisTemplate.opsForSet().add(USER_KEY_PREFIX + couponId, userId.toString());

            // 4. 异步写入订单数据库（模拟）
            sendSeckillMsg(userId, couponId);
            return "秒杀成功，订单创建中...";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "系统错误，请稍后再试";
        } finally {
        
		        // // 创建一个持久化队列
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    // 模拟异步下单处理（生产中应推送到 MQ）
    private void sendSeckillMsg(Long userId, Long couponId) {
				// 在本篇下面的 MQ 部分实现
    }
}