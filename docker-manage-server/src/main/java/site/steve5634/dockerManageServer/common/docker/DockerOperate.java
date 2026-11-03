package site.steve5634.dockerManageServer.common.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import site.steve5634.dockerManageServer.common.AssertKit;
import site.steve5634.dockerManageServer.common.config.ProjectConfig;
import site.steve5634.dockerManageServer.common.execption.BaseException;
import site.steve5634.dockerManageServer.common.file.FileUploadUtils;
import site.steve5634.dockerManageServer.common.file.FileUtils;
import site.steve5634.dockerManageServer.common.utils.StringUtils;
import site.steve5634.dockerManageServer.domain.ContainerMapping;
import site.steve5634.dockerManageServer.domain.DockerContainer;
import site.steve5634.dockerManageServer.domain.DockerImage;
import site.steve5634.dockerManageServer.domain.SearchImage;
import site.steve5634.dockerManageServer.domain.dto.CreateContainerDTO;

import java.io.*;
import java.util.*;

@Slf4j
public class DockerOperate {

    /**
     * 查询远程仓库
     *
     * @param keyWord 搜索关键字
     */
    public static List<SearchImage> queryRemoteList(DockerClient client, String keyWord) {
        List<SearchItem> itemList = client.searchImagesCmd(keyWord).exec();
        if (CollectionUtils.isEmpty(itemList)) return List.of();
        List<SearchImage> list = new ArrayList<>();
        itemList.forEach(item -> list.add(new SearchImage(item)));
        return list;
    }

    /**
     * 获取镜像列表
     */
    public static List<DockerImage> dockerImageList(DockerClient client) {
        List<Image> images = client.listImagesCmd().exec();
        if (CollectionUtils.isEmpty(images)) return List.of();
        List<DockerImage> list = new ArrayList<>();
        images.forEach(item -> list.add(new DockerImage(item)));
        return list;
    }

    /**
     * 获取镜像列表
     *
     * @param id 镜像id
     */
    public static DockerImage dockerImageById(DockerClient client, String id) {
        return new DockerImage(client.inspectImageCmd(id).exec());
    }

    /**
     * 拉取镜像（后期打算接入websocket）
     *
     * @param image 镜像名
     */
    public static void pullImage(DockerClient client, String image) {
        client.pullImageCmd(image).exec(new ResultCallback<PullResponseItem>() {
            public void onStart(Closeable closeable) {
            }

            public void onNext(PullResponseItem object) {
            }

            public void onError(Throwable throwable) {
                throw new BaseException(throwable.toString());
            }

            public void onComplete() {
            }

            public void close() {
            }
        });
    }

    /**
     * 删除镜像
     *
     * @param imageId 镜像id
     */
    public static void removeImage(DockerClient client, String imageId) {
        client.removeImageCmd(imageId).exec();
    }

    /**
     * 备份镜像
     *
     * @param name    服务器名
     * @param imageId 镜像id
     */
    public static String backImage(DockerClient client, String name, String imageId) {
        DockerImage dockerImage = dockerImageById(client, imageId);
        InputStream stream = client.saveImageCmd(imageId).exec();
        String filePath = ProjectConfig.getDownloadPath() + "/dockerImage/" + name + "/" + dockerImage.getRepository() + "@" + dockerImage.getTag() + "@" + dockerImage.getId().split(":")[1] + ".tar";
        File file = new File(filePath);
        AssertKit.isTrue(file.getParentFile().exists() || file.getParentFile().mkdirs(), "创建文件夹失败");
        try {
            FileUtils.writeBytes(stream, new FileOutputStream(filePath));
        } catch (Exception e) {
            throw new BaseException(e);
        }
        return FileUploadUtils.getPathFileName(filePath);
    }

    /**
     * 备份镜像
     *
     * @param stream     镜像输入流
     * @param repository 镜像名称
     * @param tag        镜像版本
     * @param imageId    镜像id
     */
    public static void restoreImage(DockerClient client, InputStream stream, String repository, String tag, String imageId) {
        client.loadImageCmd(stream).exec();
        client.tagImageCmd("sha256:" + imageId, repository, tag).exec();
    }

    /**
     * 创建容器 (路径映射需提前拷贝待优化)
     *
     * @param containerDTO 创建容器参数
     */
    public static void createContainers(DockerClient client, CreateContainerDTO containerDTO) {
        AssertKit.notNull(containerDTO.getImage(), "创建容器失败，未选择镜像");
        HostConfig hostConfig = HostConfig.newHostConfig();
        // 设置重启方式
        if (Integer.valueOf(1).equals(containerDTO.getRestart())) {
            hostConfig.withRestartPolicy(RestartPolicy.noRestart());
        } else if (Integer.valueOf(2).equals(containerDTO.getRestart())) {
            hostConfig.withRestartPolicy(RestartPolicy.onFailureRestart(3));
        } else if (Integer.valueOf(3).equals(containerDTO.getRestart())) {
            hostConfig.withRestartPolicy(RestartPolicy.alwaysRestart());
        } else if (Integer.valueOf(4).equals(containerDTO.getRestart())) {
            hostConfig.withRestartPolicy(RestartPolicy.unlessStoppedRestart());
        }
        // 设置容器权限
        hostConfig.withPrivileged(hostConfig.getPrivileged());
        // 设置端口映射
        List<PortBinding> portBindingList = new ArrayList<>();
        List<ExposedPort> exposedPortList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(containerDTO.getPortList())) {
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
        if (!CollectionUtils.isEmpty(containerDTO.getMountList())) {
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
        if (StringUtils.hasText(containerDTO.getName())) container.withName(containerDTO.getName());
        // 设置运行参数
        container.withEnv(containerDTO.getEnvList());
        CreateContainerResponse exec = container.exec();
        startContainer(client, exec.getId());
    }

    /**
     * 获取容器列表
     */
    public static List<DockerContainer> dockerContainerList(DockerClient client) {
        List<Container> containers = client.listContainersCmd().withShowAll(true).exec();
        if (CollectionUtils.isEmpty(containers)) return List.of();
        List<DockerContainer> list = new ArrayList<>();
        containers.forEach(item -> list.add(new DockerContainer(item)));
        return list;
    }

    /**
     * 获取容器详情
     *
     * @param containerId 容器id
     */
    public static DockerContainer dockerContainerById(DockerClient client, String containerId) {
        InspectContainerResponse response = client.inspectContainerCmd(containerId).exec();
        return new DockerContainer(response);
    }

    /**
     * 启动容器
     *
     * @param containerId 容器id
     */
    public static void startContainer(DockerClient client, String containerId) {
        client.startContainerCmd(containerId).exec();
    }

    /**
     * 暂停容器（待测试）
     *
     * @param containerId 容器id
     * */
    public static void pauseContainer(DockerClient client, String containerId){
        client.pauseContainerCmd(containerId).exec();
    }

    /**
     * 取消暂停
     *
     * @param containerId 容器id
     * */
    public static void unpauseContainer(DockerClient client, String containerId){
        client.unpauseContainerCmd(containerId).exec();
    }

    /**
     * 停止容器
     *
     * @param containerId 容器id
     */
    public static void stopContainer(DockerClient client, String containerId) {
        client.stopContainerCmd(containerId).exec();
    }

    /**
     * 重启容器
     *
     * @param containerId 容器id
     */
    public static void restartContainer(DockerClient client, String containerId) {
        client.restartContainerCmd(containerId).exec();
    }

    /**
     * 取消暂停 （待测试）
     *
     * @param containerId 容器id
     * @param name 名称
     * */
    public static void renameContainer(DockerClient client, String containerId, String name){
        client.renameContainerCmd(containerId).withName(name).exec();
    }

    /**
     * 显示日志（待开发）
     *
     * @param containerId 容器id
     */
    public static String containerLog(DockerClient client, String containerId) {
        LogContainerCmd logged = client.logContainerCmd(containerId);
        logged = logged.withStdOut(true);
        logged = logged.withStdErr(true);
        logged.withTail(100);
        logged.exec(new ResultCallback.Adapter<Frame>() {
            @Override
            public void onNext(Frame frame) {
                log.info(new String(frame.getPayload()));
            }
        });
        return "";
    }

    /**
     * 复制容器文件（容器 -> 本地）
     *
     * @param containerId 容器id
     * @param mapping 文件映射路径
     * */
    public static void copyContainerToLocal(DockerClient client, String containerId, ContainerMapping mapping){

    }

    /**
     * 复制容器文件（容器 -> 本地）
     *
     * @param containerId 容器id
     * @param mapping 文件映射路径
     * */
    public static void copyContainerFromLocal(DockerClient client, String containerId, ContainerMapping mapping){

    }

}
