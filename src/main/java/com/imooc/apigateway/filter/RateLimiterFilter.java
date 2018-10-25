package com.imooc.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.imooc.apigateway.enums.ResultEnum;
import com.imooc.apigateway.exception.RateLimiterException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 限流过滤器
 */
@Component
public class RateLimiterFilter extends ZuulFilter {
    //谷歌提供的令牌桶框架。往里面放100个令牌
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //如果没有拿到另外则抛出没有权限的异常.这个方法不需要传参里面默认取一个
        if(!RATE_LIMITER.tryAcquire()){
            throw new RateLimiterException(ResultEnum.AUTH_ERROR);
        }
        return null;
    }
}
