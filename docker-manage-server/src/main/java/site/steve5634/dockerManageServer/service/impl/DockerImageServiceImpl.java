package site.steve5634.dockerManageServer.service.impl;

import com.github.dockerjava.api.DockerClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.steve5634.dockerManageServer.common.docker.DockerClientUtils;
import site.steve5634.dockerManageServer.common.docker.DockerOperate;
import site.steve5634.dockerManageServer.common.execption.BaseException;
import site.steve5634.dockerManageServer.domain.DockerImage;
import site.steve5634.dockerManageServer.domain.DockerServer;
import site.steve5634.dockerManageServer.domain.SearchImage;
import site.steve5634.dockerManageServer.service.IDockerImageService;
import site.steve5634.dockerManageServer.service.IDockerServerService;

import java.util.List;

@Service
public class DockerImageServiceImpl implements IDockerImageService {

    @Resource
    private IDockerServerService dockerServerService;

    @Override
    public List<DockerImage> queryList(String serverId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        return DockerOperate.dockerImageList(client);
    }

    @Override
    public List<SearchImage> queryRemoteList(String serverId, String keyWord) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        return DockerOperate.queryRemoteList(client, keyWord);
    }

    @Override
    public DockerImage queryByImageId(String serverId, String imageId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        return DockerOperate.dockerImageById(client,imageId);
    }

    @Override
    public void pullImage(String serverId, String image) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.pullImage(client,image);
    }

    @Override
    public void removeImage(String serverId, String imageId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerOperate.removeImage(client,imageId);
    }

    @Override
    public String backImage(String serverId, String imageId) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        DockerServer dockerServer = dockerServerService.getById(serverId);
        return DockerOperate.backImage(client, dockerServer.getName(), imageId);
    }

    @Override
    public void restoreImage(String serverId, MultipartFile file) {
        DockerClient client = DockerClientUtils.getClient(serverId);
        try{
            String filename = file.getOriginalFilename();
            int end = filename.lastIndexOf(".");
            String[] split = filename.substring(0, end).split("@");
            DockerOperate.restoreImage(client, file.getInputStream(), split[0], split[1], split[2]);
        } catch (Exception e){
            throw new BaseException(e);
        }
    }
}
