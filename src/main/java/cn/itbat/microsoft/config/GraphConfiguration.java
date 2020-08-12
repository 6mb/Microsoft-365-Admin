package cn.itbat.microsoft.config;

import cn.itbat.microsoft.aspect.TokenCacheAspect;
import cn.itbat.microsoft.aspect.TokenCacheRedisAspect;
import cn.itbat.microsoft.provider.SimpleAuthProvider;
import com.google.common.collect.Maps;
import com.microsoft.aad.msal4j.*;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    /**
     * 配置类
     */
    private GraphProperties properties;

    private RedisTemplate<String, String> redisTemplate;

    private final static String AUTHORITY = "https://login.microsoftonline.com/";
    private final static String GRAPH_DEFAULT_SCOPE = "https://graph.microsoft.com/.default";
    private final static String CACHE_MODE = "redis";

    private static Map<String, GraphConfig> graphConfigMap = Maps.newHashMap();

    @Data
    private class GraphConfig {
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
        String cache = properties.getCache();
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
    public static synchronized IGraphServiceClient getGraphClient(String appName) {
        // Create the auth provider
        SimpleAuthProvider authProvider = new SimpleAuthProvider(GraphConfiguration.getToken(appName));

        // Create default logger to only log errors
        DefaultLogger logger = new DefaultLogger();
        logger.setLoggingLevel(LoggerLevel.DEBUG);

        // Build a Graph client
        return GraphServiceClient.builder()
                .authenticationProvider(authProvider)
                .logger(logger)
                .buildClient();
    }

    /**
     * 获取 accessToken
     *
     * @param appName 应用名
     * @return accessToken
     */
    public static String getToken(String appName) {
        try {
            return GraphConfiguration.acquireToken(appName).accessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IAuthenticationResult acquireToken(String appName) throws Exception {

        GraphConfig graphConfig = graphConfigMap.get(appName);
        if (graphConfig == null) {
            throw new RuntimeException("【get " + appName + " config error!】");
        }

        // This is the secret that is created in the Azure portal when registering the application
        IClientCredential credential = ClientCredentialFactory.createFromSecret(graphConfig.appSecret);
        ConfidentialClientApplication cca =
                ConfidentialClientApplication
                        .builder(graphConfig.appId, credential)
                        .authority(AUTHORITY + graphConfig.getAppTenant())
                        // Load token cache from file and initialize token cache aspect. The token cache will have
                        // dummy data, so the acquireTokenSilently call will fail.
                        .setTokenCacheAccessAspect(graphConfig.accessAspect)
                        .build();

        IAuthenticationResult result;
        try {
            SilentParameters silentParameters =
                    SilentParameters
                            .builder(Collections.singleton(GRAPH_DEFAULT_SCOPE))
                            .build();

            // try to acquire token silently. This call will fail since the token cache does not
            // have a token for the application you are requesting an access token for
            result = cca.acquireTokenSilently(silentParameters).join();
            log.info("【acquire token silently {} success】", appName);
        } catch (Exception ex) {
            if (ex.getCause() instanceof MsalException) {
                log.info("【acquire token silently {} error】", appName);
                ClientCredentialParameters parameters =
                        ClientCredentialParameters
                                .builder(Collections.singleton(GRAPH_DEFAULT_SCOPE))
                                .build();

                // Try to acquire a token. If successful, you should see
                // the token information printed out to console
                result = cca.acquireToken(parameters).join();
                log.info("【acquire token {} success】", appName);
            } else {
                // Handle other exceptions accordingly
                throw ex;
            }
        }
        return result;
    }


}
