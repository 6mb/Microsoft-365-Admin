package cn.itbat.microsoft.config;


import cn.itbat.microsoft.vo.BaseResultVo;
import com.microsoft.graph.http.GraphServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理
 *
 * @author log.r   (;￢＿￢)   
 * @date 2018-08-24 上午10:05
 **/
@Log4j2
@ControllerAdvice
@ResponseBody
public class ExceptionHandler {
    /**
     * 自定义异常- 业务异常
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public BaseResultVo baseExceptionHandler(RuntimeException ex, HttpServletResponse response, HttpServletRequest request) {
        log.error(">>>>>>>>>>>>【业务异常】", ex.getMessage(), ex);
        return new BaseResultVo(500, ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(GraphServiceException.class)
    public BaseResultVo graphServiceException(RuntimeException ex, HttpServletResponse response, HttpServletRequest request) {
        log.error(">>>>>>>>>>>>【微软请求异常】", ex.getMessage(), ex);
        String[] split = ex.getMessage().split("\n");
        return new BaseResultVo(500, split[0]+ split[1]);
    }

    /**
     * 500- server error
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public BaseResultVo otherExceptionHandler(Exception ex, HttpServletResponse response, HttpServletRequest request) {
        log.error(">>>>>>>>>>>>otherException", ex.getMessage(), ex);
        return BaseResultVo.error("系统异常 " + ex.getMessage());
    }

}
