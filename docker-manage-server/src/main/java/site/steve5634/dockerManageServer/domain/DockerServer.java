package site.steve5634.dockerManageServer.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("docker_server")
@Schema(description = "docker服务器配置")
public class DockerServer {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "服务器名")
    private String name;

    @Schema(description = "docker版本")
    private String version;

    @Schema(description = "服务器地址(tcp://ip:port)")
    private String host;

    @Schema(description = "是否启用https")
    private Integer tlsVerify;

    @Schema(description = "证书地址")
    private String certPath;

    @Schema(description = "仓库地址")
    private String registryUrl;

    @Schema(description = "仓库用户")
    private String registryUsername;

    @Schema(description = "仓库密码")
    private String registryPassword;

    @Schema(description = "仓库邮箱")
    private String registryEmail;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @Schema(name = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @Schema(name = "更新时间")
    private Date updateTime;

    @TableLogic
    @Schema(name = "逻辑删除")
    private Integer delFlag;

}
