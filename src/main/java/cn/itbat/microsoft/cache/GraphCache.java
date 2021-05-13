package cn.itbat.microsoft.cache;

import cn.itbat.microsoft.service.GraphService;
import cn.itbat.microsoft.vo.DirectoryRoleVo;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.microsoft.graph.models.Domain;
import com.microsoft.graph.models.SubscribedSku;
import com.microsoft.graph.models.User;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Resource
    private GraphService graphService;

    /**
     * 缓存过期时间
     */
    @Value("${graph.cache.timeout.user}")
    private long userTimeout;

    @Value("${graph.cache.timeout.license}")
    private long licenseTimeout;

    @Value("${graph.cache.timeout.domain}")
    private long domainTimeout;


    private LoadingCache<String, List<User>> usersCache;

    private LoadingCache<String, List<SubscribedSku>> licenseCache;

    private LoadingCache<String, List<Domain>> domainCache;

    private LoadingCache<String, Map<DirectoryRoleVo, Set<String>>> roleCache;

    @PostConstruct
    public void init() {
        usersCache = CacheBuilder.newBuilder()
                .expireAfterWrite(userTimeout, TimeUnit.MINUTES)
                .build(new CacheLoader<String, List<User>>() {
                    @Override
                    public List<User> load(@NonNull String key) {
                        return graphService.getUsers(key);
                    }
                });
        licenseCache = CacheBuilder.newBuilder()
                .expireAfterWrite(licenseTimeout, TimeUnit.MINUTES)
                .build(new CacheLoader<String, List<SubscribedSku>>() {
                    @Override
                    public List<SubscribedSku> load(@NonNull String key) {
                        return graphService.getSubscribedSkus(key);
                    }
                });
        domainCache = CacheBuilder.newBuilder()
                .expireAfterWrite(domainTimeout, TimeUnit.MINUTES)
                .build(new CacheLoader<String, List<Domain>>() {
                    @Override
                    public List<Domain> load(@NonNull String key) {
                        return graphService.getDomains(key);
                    }
                });
        roleCache = CacheBuilder.newBuilder()
                .expireAfterWrite(licenseTimeout, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Map<DirectoryRoleVo, Set<String>>>() {
                    @Override
                    public Map<DirectoryRoleVo, Set<String>> load(@NonNull String key) throws Exception {
                        return graphService.directoryRoleToUserNameMap(key);
                    }
                });
    }

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
     * 获取角色缓存
     *
     * @param appName 组织名称
     * @return 结果
     */
    public Map<DirectoryRoleVo, Set<String>> getRoleCache(String appName) {
        return roleCache.getUnchecked(appName);
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

    /**
     * 刷新角色缓存
     *
     * @param appName 组织名称
     */
    public void refreshRole(String appName) {
        roleCache.refresh(appName);
    }
}
