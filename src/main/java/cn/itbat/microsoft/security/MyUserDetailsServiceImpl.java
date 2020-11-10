package cn.itbat.microsoft.security;

import cn.itbat.microsoft.config.GraphProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mjj
 * @date 2020年05月11日 16:23:11
 */
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private GraphProperties graphProperties;

    /**
     * 授权的时候是对角色授权，认证的时候应该基于资源，而不是角色，因为资源是不变的，而用户的角色是会变的
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(graphProperties.getUserName(), graphProperties.getPassword(), Collections.emptyList());
    }

}