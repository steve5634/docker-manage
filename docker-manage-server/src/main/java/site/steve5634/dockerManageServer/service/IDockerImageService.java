package site.steve5634.dockerManageServer.service;

import org.springframework.web.multipart.MultipartFile;
import site.steve5634.dockerManageServer.domain.DockerImage;
import site.steve5634.dockerManageServer.domain.SearchImage;

import java.util.List;

public interface IDockerImageService {

    public List<DockerImage> queryList(String serverId);

    public List<SearchImage> queryRemoteList(String serverId, String keyWord);

    public DockerImage queryByImageId(String serverId, String imageId);

    public void pullImage(String serverId, String image);

    public void removeImage(String serverId, String imageId);

    public String backImage(String serverId, String imageId);

    public void restoreImage(String serverId, MultipartFile file);

}
