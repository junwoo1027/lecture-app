package io.lecture.redisson.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RedissonLockAop {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    private final RedissonCallNewTransaction redissonCallNewTransaction;

    public RedissonLockAop(RedissonClient redissonClient, RedissonCallNewTransaction redissonCallNewTransaction) {
        this.redissonClient = redissonClient;
        this.redissonCallNewTransaction = redissonCallNewTransaction;
    }

    @Around("@annotation(io.lecture.redisson.aop.RedissonLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock distributedLock = method.getAnnotation(RedissonLock.class);

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(),
                joinPoint.getArgs(), distributedLock.key());
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(),
                    distributedLock.timeUnit());
            if (!available) {
                return false;
            }

            return redissonCallNewTransaction.proceed(joinPoint);
        }
        catch (InterruptedException e) { // 락을 얻으려고 시도하다가 인터럽트를 받았을 때 발생
            throw new InterruptedException();
        }
        finally {
            try {
                rLock.unlock();
                log.info("unlock complete [Lock:{}] ", rLock.getName());
            }
            catch (IllegalMonitorStateException e) { // 락이 이미 종료되었을때 발생
                log.info("Redisson Lock Already Unlocked");
            }
        }
    }

}