package redis.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonLock {
    private  static  RedissonClient redissonClient;
    static {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//        config.useSingleServer().setPassword("redis1234");
        redissonClient = Redisson.create(config);
    }

    public static void getRedissionLock(String lockKey){
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            rLock.lock();
        }finally {
            rLock.unlock();
        }

    }
}
