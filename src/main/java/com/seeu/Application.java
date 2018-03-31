package com.seeu;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "com.seeu")
// 启注解事务管理
@EnableTransactionManagement
@EnableSwagger2
@EnableJpaRepositories(basePackages = "com.seeu")
@EnableAsync
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
//        JettyEmbeddedServletContainer f = new JettyEmbeddedServletContainer(new Server());
        JettyEmbeddedServletContainerFactory f = new JettyEmbeddedServletContainerFactory();
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        return f;
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/unauthorized");
            container.addErrorPages(error401Page);
        });
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${wx.app_id}")
    private String APP_ID;
    @Value("${wx.app_secret}")
    private String APP_SECRET;
    @Value("${wx.token}")
    private String TOKEN;
    @Value("${wx.aes_key}")
    private String AES_KEY;

    @Bean
    public WxMpService wxMpService() {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(APP_ID); // 设置微信公众号的appid
        config.setSecret(APP_SECRET); // 设置微信公众号的app corpSecret
        config.setToken(TOKEN); // 设置微信公众号的token
        config.setAesKey(AES_KEY); // 设置微信公众号的EncodingAESKey
        WxMpService service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(config);
        return service;
    }
}

