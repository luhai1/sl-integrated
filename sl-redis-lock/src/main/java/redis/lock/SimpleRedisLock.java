package redis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

public class SimpleRedisLock {
    private static  String SET_RESULT_OK = "OK";
    private static  Long REMOVE_RESULT_OK = 1l;
    private static  int DEFAULT_LOCK_EXP_TIME = 30;// 默认加锁时间30s
   private static Jedis jedis;
    static {
        jedis = new Jedis("127.0.0.1");
        jedis.select(14);
    }

    public static boolean setSimpleRedisLock(String lockKey,String requestId){
        return setSimpleRedisLock(lockKey,requestId,true,DEFAULT_LOCK_EXP_TIME);
    }
    public static boolean setSimpleRedisLock(String lockKey,String requestId,int expTime){
       return setSimpleRedisLock(lockKey,requestId,true,expTime);
    }
    public static boolean setSimpleRedisLock(String lockKey,String requestId,boolean nx,int expTime){
        SetParams setParams = new SetParams();
        if(nx){
            setParams.nx();//存在就不设值
            //setParams.xx(); // 强制覆盖reids值
        }
        setParams.ex(expTime);//过期时间
        String result = jedis.set(lockKey, requestId,  setParams);
        return SET_RESULT_OK.equals(result);
    }

    public static boolean removeLockKey(String lockKey,String requestId){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long deleteResult = (Long)jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return REMOVE_RESULT_OK == deleteResult;
    }


    public static void main(String[] args){
        System.out.println(setSimpleRedisLock("lockKey","111",120));
        System.out.println(removeLockKey("lockKey","111"));
    }

}
