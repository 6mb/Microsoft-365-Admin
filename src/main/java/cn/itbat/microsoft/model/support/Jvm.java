package cn.itbat.microsoft.model.support;

import lombok.Data;

/**
 * JVM
 *
 * @author mjj
 * @date 2020年05月13日 10:17:33
 */
@Data
public class Jvm {

    /**
     * 当前 JVM 占用的内存总数 (M)
     */
    private double total;

    /**
     * JVM 最大可用内存总数 (M)
     */
    private double max;

    /**
     * JVM 空闲内存 (M)
     */
    private double free;

    /**
     * JDK 版本
     */
    private String version;

    public Jvm() {
        double mb = 1024 * 1024;
        Runtime runtime = Runtime.getRuntime();
        this.total = runtime.totalMemory() / mb;
        this.free = runtime.freeMemory() / mb;
        this.max = runtime.maxMemory() / mb;
        this.version = System.getProperty("java.version");

    }

}
