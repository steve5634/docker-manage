package site.steve5634.dockerManageServer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import site.steve5634.dockerManageServer.common.utils.DateUtils;

import java.util.Date;
import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "docker镜像")
public class DockerImage {

    @Schema(description = "镜像id")
    private String id;

    @Schema(description = "镜像名称")
    private String repository;

    @Schema(description = "镜像版本")
    private String tag;

    @Schema(description = "镜像大小")
    private String size;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    public DockerImage(Image image){
        this.id = image.getId();
        if(image.getRepoTags() != null && image.getRepoTags().length!=0){
            String[] split = image.getRepoTags()[0].split(":");
            this.repository = split[0];
            this.tag = split[1];
        }
        this.size = image.getSize().toString();
        this.createTime = new Date(image.getCreated() * 1000L);
    }

    public DockerImage(InspectImageResponse response){
        this.id = response.getId();
        if(response.getRepoTags() != null && !response.getRepoTags().isEmpty()){
            String[] split = response.getRepoTags().get(0).split(":");
            this.repository = split[0];
            this.tag = split[1];
        }
        this.size = Objects.requireNonNull(response.getSize()).toString();
        this.createTime = DateUtils.parseDate(response.getCreated());
    }

}
