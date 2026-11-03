package site.steve5634.dockerManageServer.common.docker;

import com.github.dockerjava.api.DockerClient;
import site.steve5634.dockerManageServer.common.utils.SpringUtils;
import site.steve5634.dockerManageServer.domain.DockerServer;
import site.steve5634.dockerManageServer.service.IDockerServerService;

import java.util.HashMap;

public class DockerClientUtils {

    private static final HashMap<String,DockerClient> clientGroup = new HashMap<>();

    public static DockerClient getClient(String serverId){
        if(clientGroup.containsKey(serverId)) return clientGroup.get(serverId);
        IDockerServerService serverService = SpringUtils.getBean(IDockerServerService.class);
        DockerServer server = serverService.getById(serverId);
        return getClient(server);
    }

    public static DockerClient getClient(DockerServer server){
        DockerClient client = DockerClientFactory.getClient(server);
        clientGroup.put(server.getId(),client);
        return client;
    }

}
