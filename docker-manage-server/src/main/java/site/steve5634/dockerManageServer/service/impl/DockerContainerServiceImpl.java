package site.steve5634.dockerManageServer.service.impl;

import com.github.dockerjava.api.DockerClient;
import org.springframework.stereotype.Service;
import site.steve5634.dockerManageServer.common.docker.DockerClientUtils;
import site.steve5634.dockerManageServer.common.docker.DockerOperate;
import site.steve5634.dockerManageServer.domain.ContainerMapping;
import site.steve5634.dockerManageServer.domain.DockerContainer;
import site.steve5634.dockerManageServer.domain.dto.CreateContainerDTO;
import site.steve5634.dockerManageServer.service.IDockerContainerService;

import java.util.List;

@Service
public class DockerContainerServiceImpl implements IDockerContainerService {

    @Override
    public List<DockerContainer> queryList(String serverId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        return DockerOperate.dockerContainerList(client);
    }

    @Override
    public DockerContainer queryById(String serverId, String containerId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        return DockerOperate.dockerContainerById(client,containerId);
    }

    @Override
    public void createContainer(String serverId, CreateContainerDTO containerDTO) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.createContainers(client,containerDTO);
    }

    @Override
    public void startContainer(String serverId, String containerId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.startContainer(client,containerId);
    }

    @Override
    public void pauseContainer(String serverId, String containerId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.pauseContainer(client,containerId);
    }

    @Override
    public void unpauseContainer(String serverId, String containerId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.unpauseContainer(client,containerId);
    }

    @Override
    public void renameContainer(String serverId, String containerId, String name) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.renameContainer(client,containerId,name);
    }

    @Override
    public void stopContainer(String serverId, String containerId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.stopContainer(client,containerId);
    }

    @Override
    public void restartContainer(String serverId, String containerId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.restartContainer(client,containerId);
    }

    @Override
    public String containerLog(String serverId, String containerId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        return DockerOperate.containerLog(client,containerId);
    }

    @Override
    public void copyContainerToLocal(String serverId, String containerId, ContainerMapping mapping) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.copyContainerToLocal(client,containerId,mapping);
    }

    @Override
    public void copyContainerFromLocal(String serverId, String containerId, ContainerMapping mapping) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.copyContainerFromLocal(client,containerId,mapping);
    }
}
