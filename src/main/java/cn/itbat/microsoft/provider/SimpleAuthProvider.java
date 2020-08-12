package cn.itbat.microsoft.provider;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;

/**
 * @author mjj
 * @date 2020年05月11日 16:04:54
 */
public class SimpleAuthProvider implements IAuthenticationProvider {

    private String accessToken = null;

    public SimpleAuthProvider(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void authenticateRequest(IHttpRequest request) {
        // Add the access token in the Authorization header
        request.addHeader("Authorization", "Bearer " + accessToken);
    }
}