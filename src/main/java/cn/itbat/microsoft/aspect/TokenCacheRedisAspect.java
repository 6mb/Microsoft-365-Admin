package cn.itbat.microsoft.aspect;

import com.microsoft.aad.msal4j.ITokenCacheAccessAspect;
import com.microsoft.aad.msal4j.ITokenCacheAccessContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author mjj
 * @date 2020年05月12日 12:27:02
 */
public class TokenCacheRedisAspect implements ITokenCacheAccessAspect {

    private String data;

    private String appName;

    private RedisTemplate<String, String> redisTemplate;

    public TokenCacheRedisAspect(String appName, RedisTemplate<String, String> redisTemplate) {
        this.appName = "GraphToken-" + appName;
        this.redisTemplate = redisTemplate;
        this.data = redisTemplate.opsForValue().get(appName);
    }

    @Override
    public void beforeCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
        iTokenCacheAccessContext.tokenCache().deserialize(data);
    }

    @Override
    public void afterCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
        data = iTokenCacheAccessContext.tokenCache().serialize();
        // you could implement logic here to write changes to redis
        redisTemplate.opsForValue().set(appName, data, 60 * 10, TimeUnit.MINUTES);
    }

}
