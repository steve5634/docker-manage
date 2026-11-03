package site.steve5634.dockerManageServer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import site.steve5634.dockerManageServer.common.Result;
import site.steve5634.dockerManageServer.domain.ContainerMapping;
import site.steve5634.dockerManageServer.domain.DockerContainer;
import site.steve5634.dockerManageServer.domain.dto.CreateContainerDTO;
import site.steve5634.dockerManageServer.service.IDockerContainerService;

import java.util.List;

@Tag(name = "docker容器操作")
@RestController
@RequestMapping("/docker/container")
public class DockerContainerController {

    @Resource
    private IDockerContainerService containerService;

    @Operation(summary = "获取容器列表")
    @GetMapping("/queryList/{serverId}")
    public Result<List<DockerContainer>> queryList(@PathVariable("serverId") String serverId){
        List<DockerContainer> containers = containerService.queryList(serverId);
        return Result.success(containers);
    }

    @Operation(summary = "获取容器详情")
    @GetMapping("/queryById/{serverId}/{containerId}")
    public Result<DockerContainer> queryById(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId){
        DockerContainer container = containerService.queryById(serverId, containerId);
        return Result.success(container);
    }

    @Operation(summary = "创建容器")
    @PostMapping("/createContainer/{serverId}")
    public Result<String> createContainer(@PathVariable("serverId") String serverId, @RequestBody CreateContainerDTO containerDTO){
        containerService.createContainer(serverId, containerDTO);
        return Result.success();
    }

    @Operation(summary = "启动容器")
    @PostMapping("/startContainer/{serverId}/{containerId}")
    public Result<String> startContainer(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId){
        containerService.startContainer(serverId, containerId);
        return Result.success();
    }

    @Operation(summary = "停止容器")
    @PostMapping("/stopContainer/{serverId}/{containerId}")
    public Result<String> stopContainer(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId){
        containerService.stopContainer(serverId, containerId);
        return Result.success();
    }

    @Operation(summary = "重启容器")
    @PostMapping("/restartContainer/{serverId}/{containerId}")
    public Result<String> restartContainer(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId){
        containerService.restartContainer(serverId, containerId);
        return Result.success();
    }

    @Operation(summary = "暂定容器")
    @PostMapping("/pauseContainer/{serverId}/{containerId}")
    public Result<String> pauseContainer(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId){
        containerService.pauseContainer(serverId, containerId);
        return Result.success();
    }

    @Operation(summary = "取消暂定")
    @PostMapping("/unpauseContainer/{serverId}/{containerId}")
    public Result<String> unpauseContainer(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId){
        containerService.unpauseContainer(serverId, containerId);
        return Result.success();
    }

    @Operation(summary = "重命名容器")
    @PostMapping("/renameContainer/{serverId}/{containerId}/{name}")
    public Result<String> renameContainer(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId, @PathVariable("name") String name){
        containerService.renameContainer(serverId, containerId, name);
        return Result.success();
    }

    @Operation(summary = "获取容器日志")
    @GetMapping("/containerLog/{serverId}/{containerId}")
    public Result<String> containerLog(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId){
        return Result.success(containerService.containerLog(serverId, containerId));
    }

    @Operation(summary = "复制容器文件（容器 -> 本地）")
    @PostMapping("/copyContainerToLocal/{serverId}/{containerId}")
    public Result<String> copyContainerToLocal(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId, @RequestBody ContainerMapping mapping){
        containerService.copyContainerToLocal(serverId, containerId, mapping);
        return Result.success();
    }

    @Operation(summary = "复制容器文件（容器 -> 本地）")
    @PostMapping("/copyContainerFromLocal/{serverId}/{containerId}")
    public Result<String> copyContainerFromLocal(@PathVariable("serverId") String serverId, @PathVariable("containerId") String containerId, @RequestBody ContainerMapping mapping){
        containerService.copyContainerFromLocal(serverId, containerId, mapping);
        return Result.success();
    }

}
