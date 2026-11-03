package site.steve5634.dockerManageServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.steve5634.dockerManageServer.domain.DockerServer;

import java.util.List;

public interface IDockerServerService extends IService<DockerServer> {

    public List<DockerServer> queryList(DockerServer server);

}
