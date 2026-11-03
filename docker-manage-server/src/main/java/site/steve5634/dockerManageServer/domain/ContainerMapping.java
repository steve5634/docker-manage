package site.steve5634.dockerManageServer.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "docker容器端口映射")
public class ContainerMapping {

    @Schema(description = "外部端口")
    private String publicValue;

    @Schema(description = "内部端口")
    private String privateValue;

}
