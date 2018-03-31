package com.seeu.configurer;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Created by neoxiaoyi.
 * User: neo
 * Date: 09/12/2017
 * Time: 4:32 AM
 * Describe:
 */

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket api() {
        Predicate<RequestHandler> selectors = null;
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("APP接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.seeu.apis.app"))
//                .paths(PathSelectors.ant("/api/v1/**"))
                .build();
    }

    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理员平台")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.seeu.apis"))
                .paths(PathSelectors.ant("/api/admin/v1/**"))
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("广东美术馆 Api Document")
                .description("广东美术馆 API 文档")
                .version("1.0")
//                .termsOfServiceUrl("http://art.seeuio.com/api/")
                .build();
    }

}
