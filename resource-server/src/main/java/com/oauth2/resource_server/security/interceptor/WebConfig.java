package com.oauth2.resource_server.security.interceptor;

import com.oauth2.resource_server.security.SecurityMode;
import com.oauth2.resource_server.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor interceptor;
    private final SecurityProperties props;

    public WebConfig(AuthorizationInterceptor interceptor,
                     SecurityProperties props) {
        this.interceptor = interceptor;
        this.props = props;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if (props.getMode() == SecurityMode.INTERCEPTOR ||
                props.getMode() == SecurityMode.MIXED) {

            registry.addInterceptor(interceptor)
                    .addPathPatterns("/**");
        }
    }
}
