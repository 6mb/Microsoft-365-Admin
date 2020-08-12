package cn.itbat.microsoft.enums;

import lombok.Getter;

/**
 * sku订阅状态
 *
 * @author mjj
 * @date 2019年09月24日 15:41:15
 */
@Getter
public enum AccountStatusEnum {

    /**
     * 禁用
     */
    DISABLE(Boolean.FALSE, "禁用"),

    /**
     * 启用
     */
    ENABLE(Boolean.TRUE, "启用"),

    ;


    private Boolean type;

    private String name;


    AccountStatusEnum(Boolean type, String name) {
        this.name = name;
        this.type = type;
    }

    public static AccountStatusEnum parse(Boolean type) {
        for (AccountStatusEnum item : values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public static String getName(Boolean type) {
        for (AccountStatusEnum item : values()) {
            if (item.getType().equals(type)) {
                return item.getName();
            }
        }
        return null;
    }
}
