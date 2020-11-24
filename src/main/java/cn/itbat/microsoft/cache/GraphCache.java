package cn.itbat.microsoft.cache;

import cn.itbat.microsoft.service.GraphService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.microsoft.graph.models.extensions.Domain;
import com.microsoft.graph.models.extensions.SubscribedSku;
import com.microsoft.graph.models.extensions.User;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存器
 *
 * @author huahui.wu
 * @date 2020年11月24日 13:46:01
 */
@Component
@Slf4j
public class GraphCache {

    /**
     * 缓存过期时间
     */
    @Value("${graph.cache.timeout.user}")
    private long userTimeout;

    @Value("${graph.cache.timeout.license}")
    private long licenseTimeout;

    @Value("${graph.cache.timeout.domain}")
    private long domainTimeout;

    @Resource
    private GraphService graphService;

    private final LoadingCache<String, List<User>> usersCache = CacheBuilder.newBuilder()
            .expireAfterWrite(userTimeout, TimeUnit.MINUTES)
            .build(new CacheLoader<String, List<User>>() {
                @Override
                public List<User> load(@NonNull String key) {
                    return graphService.getUsers(key);
                }
            });

    private final LoadingCache<String, List<SubscribedSku>> licenseCache = CacheBuilder.newBuilder()
            .expireAfterWrite(licenseTimeout, TimeUnit.MINUTES)
            .build(new CacheLoader<String, List<SubscribedSku>>() {
                @Override
                public List<SubscribedSku> load(@NonNull String key) {
                    return graphService.getSubscribedSkus(key);
                }
            });

    private final LoadingCache<String, List<Domain>> domainCache = CacheBuilder.newBuilder()
            .expireAfterWrite(domainTimeout, TimeUnit.MINUTES)
            .build(new CacheLoader<String, List<Domain>>() {
                @Override
                public List<Domain> load(@NonNull String key) {
                    return graphService.getDomains(key);
                }
            });

    /**
     * 获取用户缓存
     *
     * @param appName 组织名称
     * @return 结果
     */
    public List<User> getUsersCache(String appName) {
        return usersCache.getUnchecked(appName);
    }

    /**
     * 获取许可证缓存
     *
     * @param appName 组织名称
     * @return 结果
     */
    public List<SubscribedSku> getLicenseCache(String appName) {
        return licenseCache.getUnchecked(appName);
    }

    /**
     * 获取域名缓存
     *
     * @param appName 组织名称
     * @return 结果
     */
    public List<Domain> getDomainCache(String appName) {
        return domainCache.getUnchecked(appName);
    }

    /**
     * 刷新用户缓存
     *
     * @param appName 组织名称
     */
    public void refreshUsers(String appName) {
        usersCache.refresh(appName);
    }

    /**
     * 刷新许可证缓存
     *
     * @param appName 组织名称
     */
    public void refreshLicense(String appName) {
        licenseCache.refresh(appName);
    }

    /**
     * 刷新域名缓存
     *
     * @param appName 组织名称
     */
    public void refreshDomain(String appName) {
        domainCache.refresh(appName);
    }
}
