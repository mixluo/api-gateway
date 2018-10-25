package com.imooc.apigateway.filter;

import com.imooc.apigateway.constants.RedisConstant;
import com.imooc.apigateway.utils.CookiesUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class AuthFilter extends ZuulFilter{

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        /**
         * /order/create 只能买家访问 (cookie openid 有值)
         * /order/finish 只能卖家访问
         * /product/list 所有人可以访问
         */
        if("/order/order/create".equals(request.getRequestURI())){
            Cookie cookie = CookiesUtil.get(request,"openid");
            if(cookie == null || StringUtils.isEmpty(cookie.getValue())){
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            }
        }
        if("/order/order/finish".equals(request.getRequestURI())){
            Cookie cookie = CookiesUtil.get(request,"token");
            if(cookie == null
                    || StringUtils.isEmpty(cookie.getValue())
                        ||StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE,cookie.getValue())))){
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            }
        }
        return null;
    }
}
