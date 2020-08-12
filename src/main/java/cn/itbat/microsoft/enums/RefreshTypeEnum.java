package cn.itbat.microsoft.enums;

import lombok.Getter;

/**
 * 刷新缓存类型
 *
 * @author mjj
 * @date 2019年09月24日 15:41:15
 */
@Getter
public enum RefreshTypeEnum {

    /**
     * 禁用
     */
    USER(1, "用户"),

    /**
     * 启用
     */
    SUB(2, "订阅"),

    /**
     * 启用
     */
    DOMAIN(3, "域名"),
    ;


    private Integer type;

    private String name;


    RefreshTypeEnum(Integer type, String name) {
        this.name = name;
        this.type = type;
    }

    public static RefreshTypeEnum parse(Integer type) {
        for (RefreshTypeEnum item : values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public static String getName(Integer type) {
        for (RefreshTypeEnum item : values()) {
            if (item.getType().equals(type)) {
                return item.getName();
            }
        }
        return null;
    }
}
