package com.doantotnghiep.security;

import com.doantotnghiep.entity.Role;
import com.doantotnghiep.utils.SessionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request,authentication);
        if(response.isCommitted()){
            return ;
        }
        redirectStrategy.sendRedirect(request,response,targetUrl);
    }

    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    private String determineTargetUrl(HttpServletRequest request,Authentication authentication) {
        String url = "";
        List<String> roles = new ArrayList<>();
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) (SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities());
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            roles.add(grantedAuthority.getAuthority());
        }
        if (roles.contains("ROLE_TEACHER") || roles.contains("ROLE_ADMIN")) {
            url = "/admin/home";
            SessionUtils.getInstance().putValue(request,"userEmail",authentication.getName());
        } else if (roles.contains("ROLE_STUDENT")) {
            url = "/";
            SessionUtils.getInstance().putValue(request,"userEmail",authentication.getName());
        }
        return url;
    }
}
