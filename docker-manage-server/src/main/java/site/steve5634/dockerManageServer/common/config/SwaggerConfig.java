package site.steve5634.dockerManageServer.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger2的接口配置
 * 
 * @author Administrator
 */
@Configuration
public class SwaggerConfig {

    /**
     * 创建API
     */
    @Bean
    public OpenAPI springShopOpenApi() {
        return new OpenAPI()
                // 接口文档标题
                .info(apiInfo());
    }

    /**
     * 添加摘要信息
     */
    private Info apiInfo() {
        // 用ApiInfoBuilder进行定制
        String name = ProjectConfig.getName();
        return new Info()
                // 设置标题
                .title(name)
                // 描述
                .description(name + " REST ful APIs ")
                // 作者
                .contact(new Contact().name("steve5634").email("1657375736@qq.com"))
                // 版本
                .version("1.0.0");
    }

}