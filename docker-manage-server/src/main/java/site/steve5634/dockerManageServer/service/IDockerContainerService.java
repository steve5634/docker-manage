package site.steve5634.dockerManageServer.service;

import site.steve5634.dockerManageServer.domain.ContainerMapping;
import site.steve5634.dockerManageServer.domain.DockerContainer;
import site.steve5634.dockerManageServer.domain.dto.CreateContainerDTO;

import java.util.List;

public interface IDockerContainerService {

    public List<DockerContainer> queryList(String serverId);

    public DockerContainer queryById(String serverId, String containerId);

    public void createContainer(String serverId, CreateContainerDTO containerDTO);

    public void startContainer(String serverId, String containerId);

    public void pauseContainer(String serverId, String containerId);

    public void unpauseContainer(String serverId, String containerId);

    public void renameContainer(String serverId, String containerId, String name);

    public void stopContainer(String serverId, String containerId);

    public void restartContainer(String serverId, String containerId);

    public String containerLog(String serverId, String containerId);

    public void copyContainerToLocal(String serverId, String containerId, ContainerMapping mapping);

    public void copyContainerFromLocal(String serverId, String containerId, ContainerMapping mapping);

}
