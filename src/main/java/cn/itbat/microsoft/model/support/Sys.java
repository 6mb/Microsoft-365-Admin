package cn.itbat.microsoft.model.support;

import cn.itbat.microsoft.utils.BetweenFormater;
import cn.itbat.microsoft.utils.DateUtil;
import lombok.Builder;
import lombok.Data;

import java.lang.management.ManagementFactory;

/**
 * @author mjj
 * @date 2020年05月13日 10:17:33
 */
@Data
public class Sys {

    /**
     * 项目路径
     */
    @Builder.Default
    private String projectDir = "";

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

    /**
     * 系统版本
     */
    private String osVersion;

    /**
     * 启动时间
     */
    private String upTime;

    public Sys() {
        this.osName = System.getProperty("os.name");
        this.osArch = System.getProperty("os.arch");
        this.osVersion = System.getProperty("os.version");
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        this.upTime = DateUtil.formatBetween(uptime, BetweenFormater.Level.SECOND);
    }

}
