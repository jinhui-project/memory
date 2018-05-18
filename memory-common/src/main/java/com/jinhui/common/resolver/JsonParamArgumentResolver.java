package com.jinhui.common.resolver;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 处理单个参数json格式的接收
 *
 * @autor wsc
 * @create 2018-04-10 9:47
 **/
public class JsonParamArgumentResolver implements HandlerMethodArgumentResolver{
    private final static Logger logger = LoggerFactory.getLogger(JsonParamArgumentResolver.class);

    private static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    //判断是否支持要转换的参数类型
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JsonParam.class);
    }

    //当支持后进行相应的转换
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String body = getRequestBody(nativeWebRequest);
        return JsonPath.read(body, methodParameter.getParameterAnnotation(JsonParam.class).value());
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String jsonBody = (String) servletRequest.getAttribute(JSON_REQUEST_BODY);
        if (jsonBody == null) {
            try {
                jsonBody = IOUtils.toString(servletRequest.getInputStream());
                servletRequest.setAttribute(JSON_REQUEST_BODY, jsonBody);
            } catch (IOException e) {
                logger.info(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return jsonBody;
    }

}
