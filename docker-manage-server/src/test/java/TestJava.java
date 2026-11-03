import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import site.steve5634.dockerManageServer.DockerManageServerApplication;
import site.steve5634.dockerManageServer.common.AssertKit;
import site.steve5634.dockerManageServer.common.docker.DockerClientUtils;
import site.steve5634.dockerManageServer.common.docker.DockerOperate;
import site.steve5634.dockerManageServer.common.utils.StringUtils;
import site.steve5634.dockerManageServer.domain.ContainerMapping;
import site.steve5634.dockerManageServer.domain.DockerServer;
import site.steve5634.dockerManageServer.domain.dto.CreateContainerDTO;
import site.steve5634.dockerManageServer.service.IDockerServerService;

import java.util.ArrayList;
import java.util.List;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DockerManageServerApplication.class)
public class TestJava {

    @Resource
    private IDockerServerService serverService;

    @Test
    public void test1(){
        CreateContainerDTO containerDTO = mysql8();
        DockerServer dockerServer = serverService.getById(2);
        DockerClient client = DockerClientUtils.getClient(dockerServer);
        AssertKit.notNull(containerDTO.getImage(),"创建容器失败，未选择镜像");
        HostConfig hostConfig = newHostConfig();
        // 设置重启方式
        if(Integer.valueOf(1).equals(containerDTO.getRestart())){
            hostConfig.withRestartPolicy(RestartPolicy.noRestart());
        } else if (Integer.valueOf(2).equals(containerDTO.getRestart())){
            hostConfig.withRestartPolicy(RestartPolicy.onFailureRestart(3));
        } else if (Integer.valueOf(3).equals(containerDTO.getRestart())){
            hostConfig.withRestartPolicy(RestartPolicy.alwaysRestart());
        } else if (Integer.valueOf(4).equals(containerDTO.getRestart())){
            hostConfig.withRestartPolicy(RestartPolicy.unlessStoppedRestart());
        }
        // 设置容器权限
        hostConfig.withPrivileged(hostConfig.getPrivileged());
        // 设置端口映射
        List<PortBinding> portBindingList = new ArrayList<>();
        List<ExposedPort> exposedPortList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(containerDTO.getPortList())){
            containerDTO.getPortList().forEach(item -> {
                ExposedPort tcp = ExposedPort.tcp(Integer.parseInt(item.getPublicValue()));
                Ports.Binding binding = Ports.Binding.bindPort(Integer.parseInt(item.getPrivateValue()));
                PortBinding ports = new PortBinding(binding, tcp);
                portBindingList.add(ports);
                exposedPortList.add(tcp);
            });
            hostConfig.withPortBindings(portBindingList);
        }
        // 设置文件映射
        List<Bind> binds = new ArrayList<>();
        List<Volume> volumes = new ArrayList<>();
        if(!CollectionUtils.isEmpty(containerDTO.getMountList())){
            containerDTO.getMountList().forEach(item -> {
                Volume volume = new Volume(item.getPublicValue());
                Bind bind = new Bind(item.getPrivateValue(), volume);
                binds.add(bind);
                volumes.add(volume);
            });
            hostConfig.withBinds(binds);
        }
        CreateContainerCmd container = client.createContainerCmd(containerDTO.getImage());
        container.withHostConfig(hostConfig);
        // 绑定端口映射
        container.withExposedPorts(exposedPortList);
        // 绑定文件映射
        container.withVolumes(volumes);
        // 设置名称
        if(StringUtils.hasText(containerDTO.getName())) container.withName(containerDTO.getName());
        // 设置运行参数
        container.withEnv(containerDTO.getEnvList());
        CreateContainerResponse exec = container.exec();
        DockerOperate.startContainer(client,exec.getId());
    }

    private CreateContainerDTO mysql8(){
        CreateContainerDTO containerDTO = new CreateContainerDTO();
        containerDTO.setImage("mysql:8.0");
        containerDTO.setName("mysql8.0");
        containerDTO.setRestart(1);
        containerDTO.setPrivileged(false);
        List<String> envList = new ArrayList<>();
        envList.add("MYSQL_ROOT_PASSWORD=chen3613");
        containerDTO.setEnvList(envList);
        List<ContainerMapping> portList = new ArrayList<>();
        portList.add(new ContainerMapping("3306","3360"));
        containerDTO.setPortList(portList);
        List<ContainerMapping> mountList = new ArrayList<>();
        mountList.add(new ContainerMapping("/docker/mysql8.0/log","/var/log/mysql"));
        mountList.add(new ContainerMapping("/docker/mysql8.0/data","/var/lib/mysql"));
        mountList.add(new ContainerMapping("/docker/mysql8.0/conf","/etc/mysql/my.conf"));
        containerDTO.setMountList(mountList);
        return containerDTO;
    }

}
