package site.steve5634.dockerManageServer.common.file;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import site.steve5634.dockerManageServer.common.AssertKit;
import site.steve5634.dockerManageServer.common.config.ProjectConfig;
import site.steve5634.dockerManageServer.common.constant.Constants;
import site.steve5634.dockerManageServer.common.utils.StringUtils;
import site.steve5634.dockerManageServer.common.execption.BaseException;

/**
 * 文件处理工具类
 *
 * @author Administrator
 */
@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {

    private static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    private static final String UTF_8 = "utf-8";

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException(filePath);
        writeBytes(new FileInputStream(file),os);
    }

    /**
     * 输出指定文件的byte数组
     *
     * @param inputStream   文件流
     * @param os            输出流
     * @return
     */
    public static void writeBytes(InputStream inputStream, OutputStream os) throws IOException {
        try {
            byte[] b = new byte[1024 * 1024];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.write(inputStream.read());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 检查文件是否可下载
     *
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    public static boolean checkAllowDownload(String resource) {
        // 禁止目录上跳级别
        String doubleDot = "..";
        if (StringUtils.contains(resource, doubleDot)) {
            return false;
        }

        // 检查允许下载的文件规则
        if (ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource))) {
            return true;
        }

        // 不在允许下载的文件规则
        return false;
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        final String msie = "MSIE";
        if (agent.contains(msie)) {
            // IE浏览器
            filename = URLEncoder.encode(filename, UTF_8);
            filename = filename.replace("+", " ");
        } else {
            final String firefox = "Firefox";
            final String chrome = "Chrome";
            if (agent.contains(firefox)) {
                // 火狐浏览器
                filename = new String(fileName.getBytes(), "ISO8859-1");
            } else if (agent.contains(chrome)) {
                // google浏览器
                filename = URLEncoder.encode(filename, UTF_8);
            } else {
                // 其它浏览器
                filename = URLEncoder.encode(filename, UTF_8);
            }
        }
        return filename;
    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        String percentEncodedFileName = percentEncode(realFileName);
        String contentDispositionValue = "attachment;" + "filename*="  + percentEncodedFileName;
        response.setHeader("Content-disposition", contentDispositionValue);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    /**
     * 获取文件真实路径
     *
     * @author yilabao
     * @date 2021年3月3日
     * @return String
     */
    public static String getFileRealPath(String filePath) {
        AssertKit.notBlank(filePath, "filePath不能为空");
        AssertKit.isTrue(filePath.startsWith(Constants.RESOURCE_PREFIX + "/"), "filePath路径有误");
        return ProjectConfig.getFilepath() + StringUtils.substringAfter(filePath, Constants.RESOURCE_PREFIX);
    }

    /**
     * 获取文件MD5值
     *
     * @author yilabao
     * @date 2021年3月3日
     * @return String
     */
    public static String getFileMd5(String filePath) throws BaseException {
        String filePathRealPath = getFileRealPath(filePath);
        String fileMd5 = null;
        File file = new File(filePathRealPath);
        FileInputStream inputStream = null;
        AssertKit.isTrue(file.exists(), StringUtils.format("文件{}不存在", filePathRealPath));
        try {
            inputStream = new FileInputStream(file);
            fileMd5 = DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        AssertKit.notBlank(fileMd5, "文件MD5获取失败");
        return fileMd5;
    }

}
