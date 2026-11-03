package site.steve5634.dockerManageServer.common.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 * 
 * @author Administrator
 */
@Component
@ConfigurationProperties(prefix = "project")
public class ProjectConfig {

    /** 项目名称 */
    @Getter
    private static String name;

    /** 上传路径 */
    @Getter
    private static String filepath;

    public void setName(String name) {
        ProjectConfig.name = name;
    }

    public void setFilepath(String filepath) {
        ProjectConfig.filepath = filepath;
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getFilepath() + "/download";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getFilepath() + "/upload";
    }

}
