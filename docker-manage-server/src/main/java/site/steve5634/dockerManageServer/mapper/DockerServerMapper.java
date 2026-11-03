package site.steve5634.dockerManageServer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import site.steve5634.dockerManageServer.domain.DockerServer;

import java.util.List;

public interface DockerServerMapper extends BaseMapper<DockerServer> {

    List<DockerServer> queryList(DockerServer server);

}
