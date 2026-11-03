package site.steve5634.dockerManageServer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import site.steve5634.dockerManageServer.common.utils.DateUtils;

import java.util.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "docker容器")
public class DockerContainer {

    @Schema(description = "容器id")
    private String id;

    @Schema(description = "镜像id")
    private String imageId;

    @Schema(description = "镜像名称")
    private String imageName;

    @Schema(description = "容器名称")
    private String containerName;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "运行状态")
    private String state;

    @Schema(description = "端口映射")
    private List<ContainerMapping> portList;

    @Schema(description = "文件映射")
    private List<ContainerMapping> mountList;

    @Schema(description = "运行参数")
    private List<String> envList;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    public DockerContainer(Container container) {
        this.id = container.getId();
        this.imageId = container.getImageId();
        this.imageName = container.getImage();
        this.containerName = container.getNames()[0];
        this.status = container.getStatus();
        this.state = container.getState();
        Arrays.stream(container.getPorts()).forEach(item -> {
            if(item.getPublicPort()!=null) this.addPort(new ContainerMapping(item.getPublicPort().toString(),item.getIp()+":"+item.getPrivatePort()));
        });
        container.getMounts().forEach(item -> this.addMount(new ContainerMapping(item.getSource(),item.getDestination())));
        this.createTime = new Date(container.getCreated() * 1000L);
    }

    public DockerContainer(InspectContainerResponse response) {
        this.id = response.getId();
        this.imageId = response.getImageId();
        this.imageName = response.getConfig().getImage();
        this.containerName = response.getName();
        this.status = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",DateUtils.parseDateByyyyyMMddTHHmmssSSSSSSSSSZ(Objects.requireNonNull(response.getState().getStartedAt())));
        this.state = response.getState().getStatus();
        response.getNetworkSettings().getPorts().getBindings().forEach((key, value) -> {
            if(value!=null) this.addPort(new ContainerMapping(Arrays.toString(value), String.valueOf(key.getPort())));
        });
        Objects.requireNonNull(response.getMounts()).forEach(item -> this.addMount(new ContainerMapping(item.getSource(),Objects.requireNonNull(item.getDestination()).getPath())));
        this.envList = Arrays.asList(Objects.requireNonNull(response.getConfig().getEnv()));
        this.createTime = DateUtils.parseDateByyyyyMMddTHHmmssSSSSSSSSSZ(response.getCreated());
    }

    public void addPort(ContainerMapping mapping){
        if(this.portList==null) this.portList = new ArrayList<>();
        this.portList.add(mapping);
    }

    public void addMount(ContainerMapping mapping){
        if(this.mountList==null) this.mountList = new ArrayList<>();
        this.mountList.add(mapping);
    }

}
