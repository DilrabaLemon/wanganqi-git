package com.boye.common.interfaces;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
/**
 * @功能描述 aop解析注解
 * @author www.gaozz.club
 * @date 2018-08-26
 */
public class NoRepeatSubmitAop {

	private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private RedisTemplate<String, Integer> template;

    @Around("execution(* com.boye.controller.*Controller.*(..)) && @annotation(nrs)")
    public Object arround(ProceedingJoinPoint pjp, NoRepeatSubmit nrs) {

        Map<String, Object> result = new HashMap<String, Object>();

        ValueOperations<String, Integer> opsForValue = template.opsForValue();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
            HttpServletRequest request = attributes.getRequest();
            String key = sessionId + "-" + request.getServletPath();
            if (opsForValue.get(key) == null) {// 如果缓存中有这个url视为重复提交
                Object o = pjp.proceed();
                opsForValue.set(key, 0, 2, TimeUnit.SECONDS);//设置缓存时间2秒
                return o;
            } else {
                // 重复提交，将该线程挂起两秒
                Thread.sleep(2000);
                logger.error("重复提交:"+key);
//                return null;
                result.put("code", "2");
                result.put("data", "");
                result.put("msg", "重复提交");
                return result;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("验证重复提交时出现未知异常!");
//            return "{\"code\":-889,\"message\":\"验证重复提交时出现未知异常!\"}";
             result.put("code", "2");
             result.put("data", "");
             result.put("msg", "验证重复提交时出现未知异常!");
            return result;
        }

    }

}

