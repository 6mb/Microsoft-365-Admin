package cn.itbat.microsoft.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回封装对象
 *
 * @author mjj   (;￢＿￢)  
 * @version 1.0 
 * @date 2019-07-10 11:34
 **/
@Data
public class BaseResultVo implements Serializable {
    /**
     * 状态
     */
    private int status = SUCCESS_CODE;

    /**
     * 消息
     */
    private String message = SUCCESS_MSG;

    /**
     * 数据
     */
    private Object data;

    public static final Integer SUCCESS_CODE = 200;
    public static final Integer ERROR_CODE = 500;
    public static final Integer NOT_LOGIN = 300;
    public static final Integer PAYMENT_FAILURE = 301;

    public static final String SUCCESS_MSG = "操作成功";
    public static final String ERROR_MSG = "操作失败";
    public static final String NOT_LOGIN_MSG = "未登录";


    public BaseResultVo(Object data, int status, String message) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public BaseResultVo(Object data) {
        this.data = data;
    }

    public BaseResultVo(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseResultVo() {
    }


    public static BaseResultVo success() {
        return new BaseResultVo(SUCCESS_CODE, SUCCESS_MSG);
    }

    public static BaseResultVo success(Object data) {
        return new BaseResultVo(data, SUCCESS_CODE, SUCCESS_MSG);
    }

    public static BaseResultVo error(String message) {
        return new BaseResultVo(ERROR_CODE, message);
    }

    public static BaseResultVo error() {
        BaseResultVo baseResultVo = new BaseResultVo();
        baseResultVo.setStatus(BaseResultVo.ERROR_CODE);
        baseResultVo.setMessage(BaseResultVo.ERROR_MSG);
        return baseResultVo;
    }

    public static BaseResultVo notLogin() {
        return new BaseResultVo(NOT_LOGIN, NOT_LOGIN_MSG);
    }

    public static BaseResultVo error(Integer code, String message) {
        return new BaseResultVo(code, message);
    }
}
