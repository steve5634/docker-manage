package site.steve5634.dockerManageServer.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import site.steve5634.dockerManageServer.domain.ContainerMapping;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateContainerDTO {

    @Schema(description = "容器")
    private String image;

    @Schema(description = "容器名称")
    private String name;

    @Schema(description = "重启模式(1:不重启 2:故障退出时重启 3:总是重启 4:总是重启没有手动停掉的容器)")
    private Integer restart;

    @Schema(description = "容器权限")
    private Boolean privileged;

    @Schema(description = "运行参数")
    private List<String> envList;

    @Schema(description = "端口映射")
    private List<ContainerMapping> portList;

    @Schema(description = "文件映射")
    private List<ContainerMapping> mountList;

}
