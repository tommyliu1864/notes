package com.my.system.aop;

import com.alibaba.fastjson.JSON;
import com.my.common.utils.IpUtil;
import com.my.common.utils.JwtHelper;
import com.my.model.po.system.SysOperLog;
import com.my.system.service.SysOperLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Aspect
@Component
/**
 * 操作日志的AOP处理
 */
public class LogAspect {

    @Autowired
    private SysOperLogService sysOperLogService;

    // 操作成功日志记录，这里我们复用swagger文档的注解信息，作为日志的部分信息
    @AfterReturning(pointcut = "@annotation(apiOperation)", returning = "result")
    public void doSucceedLog(JoinPoint joinPoint, ApiOperation apiOperation, Object result) {
        SysOperLog sysOperLog = buildLog(joinPoint, apiOperation, JSON.toJSONString(result), 1, null);
        sysOperLogService.save(sysOperLog);
    }

    // 操作异常，日志记录
    @AfterThrowing(pointcut = "@annotation(apiOperation)", throwing = "e")
    public void doFailedLog(JoinPoint joinPoint, ApiOperation apiOperation, Exception e) {
        // 消息太长，截断处理
        String errorMsg = substring(e.getMessage(), 500);
        SysOperLog sysOperLog = buildLog(joinPoint, apiOperation, null, 0, errorMsg);
        sysOperLogService.save(sysOperLog);
    }

    /**
     * 创建日志对象
     *
     * @param joinPoint
     * @param apiOperation
     * @param result
     * @return
     */
    private SysOperLog buildLog(
            JoinPoint joinPoint,
            ApiOperation apiOperation,
            String result,
            int status,
            String errorMsg) {
        SysOperLog log = new SysOperLog();
        // 模块注释
        Api api = joinPoint.getTarget().getClass().getAnnotation(Api.class);
        log.setModule(String.join(",", api.tags()));
        // 方法注释
        log.setTitle(apiOperation.value());
        // 方法名称
        String method = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        log.setMethod(method);
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 用户名
        String token = request.getHeader("token");
        String username = JwtHelper.getUsername(token);
        log.setUsername(username);
        // ip
        String ip = IpUtil.getIpAddress(request);
        log.setIpaddr(ip);
        log.setStatus(status);
        log.setErrorMsg(errorMsg);

        // 请求参数
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            String param = argsArrayToString(args);
            // 如果参数太长，截断处理
            log.setParam(substring(param, 1500));
        }

        // 只有修改操作，才保存返回值，因为查询操作的返回值太多了
        String requestMethod = request.getMethod();
        if (HttpMethod.PUT.name().equals(requestMethod)
                || HttpMethod.POST.name().equals(requestMethod)
                || HttpMethod.DELETE.name().equals(requestMethod)) {
            // 方法返回值
            if (result != null) {
                log.setResult(result);
            }
        }
        return log;
    }


    /**
     * 将请求参数拼接为JSON字符串
     *
     * @param paramsArray
     * @return
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (!StringUtils.isEmpty(o) && !isFilterObject(o)) {
                    try {
                        Object jsonObj = JSON.toJSON(o);
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }

    /**
     * 截取字符串
     *
     * @param str
     * @param length 超出指定长度范围，才截取字符串
     * @return 被截断的字符串
     */
    public String substring(String str, int length) {
        if (str.length() > length) {
            return str.substring(0, length);
        }
        return str;
    }

}

