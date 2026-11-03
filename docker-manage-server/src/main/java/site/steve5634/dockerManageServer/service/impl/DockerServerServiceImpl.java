package site.steve5634.dockerManageServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.steve5634.dockerManageServer.domain.DockerServer;
import site.steve5634.dockerManageServer.mapper.DockerServerMapper;
import site.steve5634.dockerManageServer.service.IDockerServerService;

import java.util.List;

@Service
public class DockerServerServiceImpl extends ServiceImpl<DockerServerMapper, DockerServer> implements IDockerServerService {

    @Override
    public List<DockerServer> queryList(DockerServer server) {
        return getBaseMapper().queryList(server);
    }

}
