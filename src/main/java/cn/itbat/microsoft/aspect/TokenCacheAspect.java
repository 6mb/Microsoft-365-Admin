package cn.itbat.microsoft.aspect;

import com.microsoft.aad.msal4j.ITokenCacheAccessAspect;
import com.microsoft.aad.msal4j.ITokenCacheAccessContext;

import java.util.HashMap;

/**
 * TokenCacheAspect
 *
 * @author mjj
 * @date 2020年05月12日 09:16:33
 */
public class TokenCacheAspect implements ITokenCacheAccessAspect {

    private HashMap<String, String> data = new HashMap<>();

    private String appName;

    public TokenCacheAspect(String appName) {
        this.data.get(appName);
        this.appName = appName;
    }

    @Override
    public void beforeCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
        iTokenCacheAccessContext.tokenCache().deserialize(data.get(appName));
    }

    @Override
    public void afterCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
        data.put(appName, iTokenCacheAccessContext.tokenCache().serialize());
    }
}