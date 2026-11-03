package site.steve5634.dockerManageServer.common.file;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import site.steve5634.dockerManageServer.common.AssertKit;
import site.steve5634.dockerManageServer.common.config.ProjectConfig;
import site.steve5634.dockerManageServer.common.constant.Constants;
import site.steve5634.dockerManageServer.common.execption.BaseException;
import site.steve5634.dockerManageServer.common.execption.InvalidExtensionException;
import site.steve5634.dockerManageServer.common.utils.DateUtils;
import site.steve5634.dockerManageServer.common.utils.StringUtils;
import site.steve5634.dockerManageServer.common.uuid.IdUtils;

import java.io.File;
import java.io.IOException;

public class FileUploadUtils {

    private static final String defaultBaseDir = ProjectConfig.getUploadPath();

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     */
    public static String upload(MultipartFile file) throws IOException {
        try {
            return upload(defaultBaseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir          相对应用的基目录
     * @param file             上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     */
    public static String upload(String baseDir, MultipartFile file, String[] allowedExtension) {
        String pathFileName;
        try{
            assertAllowed(file, allowedExtension);
            String fileName = extractFilename(file);
            pathFileName = getPathFileName(baseDir, fileName);
            File desc = getAbsoluteFile(baseDir, fileName);
            file.transferTo(desc.getCanonicalFile());
        } catch (Exception e){
            throw new BaseException(e.toString());
        }
        return pathFileName;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     */
    public static void assertAllowed(MultipartFile file, String[] allowedExtension) throws InvalidExtensionException {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension, fileName);
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension, fileName);
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension, fileName);
            } else {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension) && file.getContentType() != null) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file) {
        String extension = getExtension(file);
        return generateFilename(extension);
    }

    /**
     * 生成文件名
     *
     * @param extension 文件扩展名
     * @return String
     * @author yilabao
     * @date 2021年4月13日
     */
    public static String generateFilename(String extension) {
        return DateUtils.datePath() + "/" + IdUtils.fastUuid() + "." + extension;
    }

    private static String getPathFileName(String uploadDir, String fileName) {
        int dirLastIndex = ProjectConfig.getFilepath().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        return Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
    }

    public static String getPathFileName(String uploadDir) {
        if(!uploadDir.startsWith(ProjectConfig.getFilepath())) return uploadDir;
        return uploadDir.replace(ProjectConfig.getFilepath(),Constants.RESOURCE_PREFIX);
    }

    private static File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + "/" + fileName);
        AssertKit.isTrue(desc.getParentFile().exists() || desc.getParentFile().mkdirs(),"创建文件夹失败："+desc.getParentFile());
        AssertKit.isTrue(desc.exists() || desc.createNewFile(),"创建文件失败："+desc.getPath());
        return desc;
    }

}
