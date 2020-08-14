package cn.itbat.microsoft.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Graph 配置
 *
 * @author mjj
 * @date 2020年05月11日 16:23:11
 */
@Data
@ConfigurationProperties(prefix = "graph")
public class GraphProperties {

    /**
     * 缓存方式
     */
    private String cache;

    /**
     * 小程序配置
     */
    private List<GraphConfig> configs;

    @Data
    public static class GraphConfig {

        /**
         * 名称
         */
        private String appName;

        /**
         * client id
         */
        private String appId;

        /**
         * Secret
         */
        private String appSecret;

        /**
         * Tenant
         */
        private String appTenant;

        /**
         * admin 账户
         */
        private String admin;
    }

    /**
     * Office 配置
     *
     * @param appName office 名称
     * @return 配置
     */
    public GraphProperties.GraphConfig getConfig(String appName) {
        for (GraphProperties.GraphConfig maConfig : configs) {
            if (maConfig.getAppName().equals(appName)) {
                return maConfig;
            }
        }
        return configs.get(0);
    }

    /**
     * Office 订阅配置
     */
    private List<GraphSubConfig> subscribed;

    @Data
    public static class GraphSubConfig {

        /**
         * skuName
         */
        private String skuName;

        /**
         * 显示名称
         */
        private String displayName;

        /**
         * skuId
         */
        private String skuId;

    }

    /**
     * 获取对应订阅的配置
     *
     * @param skuName 订阅名称
     * @return 配置
     */
    public GraphProperties.GraphSubConfig getSubConfig(String skuName) {
        for (GraphProperties.GraphSubConfig maConfig : subscribed) {
            if (maConfig.getSkuName().equals(skuName)) {
                return maConfig;
            }
        }
        return subscribed.get(0);
    }

    public String getSubConfigName(String skuId) {
        for (GraphProperties.GraphSubConfig maConfig : subscribed) {
            if (maConfig.getSkuId().equals(skuId)) {
                return maConfig.getSkuName();
            }
        }
        return skuId;
    }

    public String getSubConfigDisplayName(String skuId) {
        for (GraphProperties.GraphSubConfig maConfig : subscribed) {
            if (maConfig.getSkuId().equals(skuId)) {
                return maConfig.getDisplayName();
            }
        }
        return null;
    }


}
