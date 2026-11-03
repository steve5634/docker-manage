package site.steve5634.dockerManageServer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.steve5634.dockerManageServer.common.Result;
import site.steve5634.dockerManageServer.domain.DockerImage;
import site.steve5634.dockerManageServer.domain.SearchImage;
import site.steve5634.dockerManageServer.service.IDockerImageService;

import java.util.List;

@Tag(name = "docker镜像操作")
@RestController
@RequestMapping("/docker/image")
public class DockerImageController {

    @Resource
    private IDockerImageService imageService;

    @Operation(summary = "查询本机镜像列表")
    @GetMapping("/queryList/{serverId}")
    public Result<List<DockerImage>> queryList(@PathVariable("serverId") String serverId){
        return Result.success(imageService.queryList(serverId));
    }

    @Operation(summary = "查询仓库镜像列表")
    @GetMapping("/queryRemoteList/{serverId}/{keyWord}")
    public Result<List<SearchImage>> queryRemoteList(@PathVariable("serverId") String serverId, @PathVariable("keyWord") String keyWord){
        return Result.success(imageService.queryRemoteList(serverId,keyWord));
    }

    @Operation(summary = "查询本机镜像详情")
    @GetMapping("/queryByImageId/{serverId}/{imageId}")
    public Result<DockerImage> queryByImageId(@PathVariable("serverId") String serverId, @PathVariable("imageId") String imageId){
        return Result.success(imageService.queryByImageId(serverId,imageId));
    }

    @Operation(summary = "拉取镜像")
    @PostMapping("/pullImage/{serverId}/{image}")
    public Result<String> pullImage(@PathVariable("serverId") String serverId, @PathVariable("image") String image){
        imageService.pullImage(serverId,image);
        return Result.success();
    }

    @Operation(summary = "删除镜像")
    @PostMapping("/removeImage/{serverId}/{imageId}")
    public Result<String> removeImage(@PathVariable("serverId") String serverId, @PathVariable("imageId") String image){
        imageService.removeImage(serverId,image);
        return Result.success();
    }

    @Operation(summary = "备份镜像")
    @PostMapping("/backImage/{serverId}/{imageId}")
    public Result<String> backImage(@PathVariable("serverId") String serverId, @PathVariable("imageId") String imageId){
        String imagePath = imageService.backImage(serverId, imageId);
        return Result.success(imagePath);
    }

    @Operation(summary = "还原镜像")
    @PostMapping("/restoreImage/{serverId}")
    public Result<String> restoreImage(@PathVariable("serverId") String serverId, MultipartFile file){
        imageService.restoreImage(serverId, file);
        return Result.success();
    }

}
