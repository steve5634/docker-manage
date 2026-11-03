package site.steve5634.dockerManageServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.steve5634.dockerManageServer.common.execption.BaseException;
import site.steve5634.dockerManageServer.common.file.FileUploadUtils;
import site.steve5634.dockerManageServer.domain.FileSource;
import site.steve5634.dockerManageServer.mapper.FileSourceMapper;
import site.steve5634.dockerManageServer.service.IFileSourceService;

import java.math.BigDecimal;

@Service
public class FileSourceServiceImpl extends ServiceImpl<FileSourceMapper,FileSource> implements IFileSourceService {

    @Override
    public FileSource uploadFile(MultipartFile file) {
        FileSource source = new FileSource();
        try{
            String filename = file.getOriginalFilename();
            BigDecimal decimal = new BigDecimal(file.getSize());
            String path = FileUploadUtils.upload(file);
            source.setName(filename);
            source.setSize(decimal);
            source.setPath(path);
            getBaseMapper().insert(source);
        } catch (Exception e){
            throw new BaseException(e.getMessage());
        }
        return source;
    }

}
