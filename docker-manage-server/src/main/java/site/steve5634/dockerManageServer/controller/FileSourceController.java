package site.steve5634.dockerManageServer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.steve5634.dockerManageServer.common.AssertKit;
import site.steve5634.dockerManageServer.common.Result;
import site.steve5634.dockerManageServer.common.config.ProjectConfig;
import site.steve5634.dockerManageServer.common.file.FileUtils;
import site.steve5634.dockerManageServer.common.utils.StringUtils;
import site.steve5634.dockerManageServer.domain.FileSource;
import site.steve5634.dockerManageServer.service.IFileSourceService;

@Slf4j
@Tag(name = "文件服务")
@RestController
@RequestMapping("/file")
public class FileSourceController {

    @Resource
    private IFileSourceService sourceService;

    @Operation(description = "通用下载文件")
    @GetMapping("/download")
    public void downloadFile(String fileName, Boolean delete, HttpServletResponse response) {
        try {
            AssertKit.isTrue(FileUtils.checkAllowDownload(fileName),StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("/") + 1);
            String filePath = ProjectConfig.getDownloadPath() + fileName;
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) FileUtils.deleteFile(filePath);
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    @Operation(description = "通用上传文件")
    @PostMapping("/upload")
    public Result<FileSource> uploadFile(MultipartFile file) {
        FileSource source = sourceService.uploadFile(file);
        return Result.success(source);
    }

}
