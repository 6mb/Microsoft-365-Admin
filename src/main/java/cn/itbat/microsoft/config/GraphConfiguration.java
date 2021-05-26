package cn.itbat.microsoft.config;

import cn.itbat.microsoft.aspect.TokenCacheAspect;
import cn.itbat.microsoft.aspect.TokenCacheRedisAspect;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.google.common.collect.Maps;
import com.microsoft.aad.msal4j.*;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Graph 初始化配置
 *
 * @author mjj
 * @date 2020年05月12日 10:52:56
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(GraphProperties.class)
public class GraphConfiguration {

    @Value("${graph.cache.token}")
    private String cache;
    /**
     * 配置类
     */
    private final GraphProperties properties;

    private final RedisTemplate<String, String> redisTemplate;

    private final static String AUTHORITY = "https://login.microsoftonline.com/";
    private final static String GRAPH_DEFAULT_SCOPE = "https://graph.microsoft.com/.default";
    private final static String CACHE_MODE = "redis";

    private static Map<String, GraphConfig> graphConfigMap = Maps.newHashMap();

    @Data
    private static class GraphConfig {
        private String appName;
        private String appId;
        private String appSecret;
        private String appTenant;
        private ITokenCacheAccessAspect accessAspect;
    }

    @Autowired
    public GraphConfiguration(GraphProperties properties, RedisTemplate<String, String> redisTemplate) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        List<GraphProperties.GraphConfig> configs = this.properties.getConfigs();
        graphConfigMap = configs.stream()
                .map(a -> {
                    GraphConfig config = new GraphConfig();
                    // 默认使用内存存储token
                    if (CACHE_MODE.equals(cache)) {
                        config.setAccessAspect(new TokenCacheRedisAspect(a.getAppName(), redisTemplate));
                    } else {
                        config.setAccessAspect(new TokenCacheAspect(a.getAppName()));
                    }
                    config.setAppName(a.getAppName());
                    config.setAppId(a.getAppId());
                    config.setAppSecret(a.getAppSecret());
                    config.setAppTenant(a.getAppTenant());
                    return config;
                }).collect(Collectors.toMap(GraphConfig::getAppName, a -> a));
    }

    /**
     * 构建 GraphClient
     *
     * @param appName 应用名
     * @return IGraphServiceClient
     */
    public static synchronized GraphServiceClient<Request> getGraphClient(String appName) {
        // Create the auth provider
//        SimpleAuthProvider authProvider = new SimpleAuthProvider(GraphConfiguration.getToken(appName));
        GraphConfig config = graphConfigMap.get(appName);
        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(config.appId)
                .clientSecret(config.appSecret)
                .tenantId(config.appTenant)
                .build();

        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(Collections.singletonList(GRAPH_DEFAULT_SCOPE), clientSecretCredential);

        // Create default logger to only log errors
        DefaultLogger logger = new DefaultLogger();
        logger.setLoggingLevel(LoggerLevel.DEBUG);

        log.info("Graph client is built!");
        // Build a Graph client
        return GraphServiceClient.builder()
                .authenticationProvider(tokenCredentialAuthProvider)
                .logger(logger)
                .buildClient();
    }



}
