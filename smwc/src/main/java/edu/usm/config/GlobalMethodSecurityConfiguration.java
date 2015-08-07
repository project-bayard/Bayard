package edu.usm.config;

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;

import javax.annotation.Resource;

/**
 * Created by scottkimball on 8/6/15.
 */
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled=true)
public class GlobalMethodSecurityConfiguration extends SecurityConfig {

    @Resource
    private RoleHierarchy roleHierarchy;

    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }




}