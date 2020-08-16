package cn.itbat.microsoft.model.support;

import com.sun.management.OperatingSystemMXBean;
import lombok.Data;

import java.lang.management.ManagementFactory;

/**
 * @author mjj
 * @date 2020年05月13日 10:17:33
 */
@Data
public class Mem {

    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public Mem() {
        double mb = 1024 * 1024;
        OperatingSystemMXBean osb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 总的物理内存+虚拟内存
        long totalVirtualMemory = osb.getTotalSwapSpaceSize();
        // 剩余的物理内存
        long freePhysicalMemorySize = osb.getFreePhysicalMemorySize();
        this.total = totalVirtualMemory / mb;
        this.free = freePhysicalMemorySize / mb;
        this.used = (totalVirtualMemory - freePhysicalMemorySize) / mb;
    }


}
