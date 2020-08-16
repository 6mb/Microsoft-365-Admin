package cn.itbat.microsoft.model.support;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统信息
 *
 * @author mjj
 * @date 2020年05月13日 10:17:33
 */
@Data
public class SystemMonitorInfo implements Serializable {

    /**
     * 服务器基本信息
     */
    private Sys sys;

    /**
     * JVM 信息
     */
    private Jvm jvm;

    /**
     * 系统内存
     */
    private Mem mem;

    public SystemMonitorInfo() {
        this.jvm = new Jvm();
        this.sys = new Sys();
        this.mem = new Mem();
    }

}
