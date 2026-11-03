package site.steve5634.dockerManageServer.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.steve5634.dockerManageServer.common.PageDomain;
import site.steve5634.dockerManageServer.common.Result;
import site.steve5634.dockerManageServer.domain.DockerServer;
import site.steve5634.dockerManageServer.service.IDockerServerService;

import java.util.Arrays;
import java.util.List;

@Tag(name = "docker服务器配置")
@RestController
@RequestMapping("/docker/server")
public class DockerServerController {

    @Resource
    private IDockerServerService dockerServer;

    @Operation(summary = "查询docker服务器配置分页")
    @GetMapping("/queryPage")
    public Result<PageInfo<DockerServer>> queryPage(DockerServer server, @Validated PageDomain page){
        PageInfo<DockerServer> pageInfo = PageHelper.startPage(page.getPageNum(), page.getPageSize()).doSelectPageInfo(() -> dockerServer.queryList(server));
        return Result.success(pageInfo);
    }

    @Operation(summary = "查询docker服务器配置列表")
    @GetMapping("/queryList")
    public Result<List<DockerServer>> queryList(DockerServer server){
        return Result.success(dockerServer.queryList(server));
    }

    @Operation(summary = "新增docker服务器配置")
    @PostMapping("/save")
    public Result<String> save(@RequestBody DockerServer server){
        dockerServer.save(server);
        return Result.success();
    }

    @Operation(summary = "新增docker服务器配置")
    @PostMapping("/update")
    public Result<String> update(@RequestBody DockerServer server){
        dockerServer.updateById(server);
        return Result.success();
    }

    @Operation(summary = "删除docker服务器配置")
    @PostMapping("/delete/{ids}")
    public Result<String> delete(@PathVariable("ids") String ids){
        dockerServer.removeBatchByIds(Arrays.asList(ids.split(",")));
        return Result.success();
    }

}
