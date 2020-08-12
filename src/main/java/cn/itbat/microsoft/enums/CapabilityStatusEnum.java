package cn.itbat.microsoft.enums;

import lombok.Getter;

/**
 * sku订阅状态
 *
 * @author mjj
 * @date 2019年09月24日 15:41:15
 */
@Getter
public enum CapabilityStatusEnum {

    /**
     * 禁用
     */
    ENABLE("Enabled", "启用"),

    /**
     * 启用
     */
    WARNING("Warning", "警告"),

    /**
     * 启用
     */
    SUSPENDED("Suspended", "暂停"),

    /**
     * 启用
     */
    DELETED("Deleted", "删除"),

    /**
     * 启用
     */
    LOCKED_OUT("锁定", "锁定"),


    ;


    private String type;

    private String name;


    CapabilityStatusEnum(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public static CapabilityStatusEnum parse(String type) {
        for (CapabilityStatusEnum item : values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public static String getName(String type) {
        for (CapabilityStatusEnum item : values()) {
            if (item.getType().equals(type)) {
                return item.getName();
            }
        }
        return null;
    }
}
