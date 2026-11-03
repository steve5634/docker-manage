package site.steve5634.dockerManageServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import site.steve5634.dockerManageServer.domain.FileSource;

public interface IFileSourceService extends IService<FileSource> {

    public FileSource uploadFile(MultipartFile file);

}
