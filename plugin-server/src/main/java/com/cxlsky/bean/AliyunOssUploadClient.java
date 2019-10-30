package com.cxlsky.bean;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.CRC64;
import com.aliyun.oss.model.PutObjectResult;
import com.cxlsky.exception.FileUploadException;
import com.cxlsky.properties.AliyunOssProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Slf4j
public class AliyunOssUploadClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 生成新的文件名称。
     */
    private final SnowflakeIdWorker idWorker = new SnowflakeIdWorker();
    private final static String[] AUDIO_EXTENSIONS = {"mp3", "wav", "silk"};
    private final static String[] IMAGE_EXTENSIONS = {"jpg", "png", "jpeg"};
    private final OSS ossClient;
    private final AliyunOssProperties aliyunOssProperties;
    private final double unitSize = 1024;

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final TaskExecutor executor;

    private static String schema = "https://";


    public AliyunOssUploadClient(OSS ossClient, AliyunOssProperties aliyunOssProperties, TaskExecutor executor) {
        this.ossClient = ossClient;
        this.aliyunOssProperties = aliyunOssProperties;
        this.executor = executor;
    }

    private final static String PREFIX = "upload";
    private final static String FORMAT = "/yyyyMM/dd/";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);

    /**
     * 基于spring-mvc 的文件上传
     *
     * @param file
     * @param sync 是否同步，默认异步上传
     */
    public String uploadMultipartFile(MultipartFile file, boolean sync) {
        String c = "";
        String contentType = file.getContentType();
        if (contentType != null) {
            c = contentType.substring(0, contentType.indexOf("/"));
        }
        String path = PREFIX + "/" + c + simpleDateFormat.format(new Date()) + idWorker.nextId() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        if (sync) {
            try {
                putObjectToOss(file, path);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        } else {
            executor.execute(() -> {
                try {
                    putObjectToOss(file, path);
                } catch (FileUploadException e) {
                    e.printStackTrace();
                }
            });
        }
        return schema + aliyunOssProperties.getBucket() + "." + aliyunOssProperties.getSuffix() + "/" + path;
    }

    public String uploadFile(File file, boolean sync) {
        String path = PREFIX + simpleDateFormat.format(new Date()) + idWorker.nextId() + "." + FilenameUtils.getExtension(file.getName());
        if (sync) {
            try {
                uploadFileToOss(path, file);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } finally {
                FileUtils.deleteQuietly(file);
            }
        } else {
            executor.execute(() -> {
                try {
                    uploadFileToOss(path, file);
                } catch (FileUploadException | JsonProcessingException e) {
                    e.printStackTrace();
                } finally {
                    FileUtils.deleteQuietly(file);
                }
            });
        }
        return schema + aliyunOssProperties.getBucket() + "." + aliyunOssProperties.getSuffix() + "/" + path;
    }

    /**
     * @param file
     * @return
     */
    private void putObjectToOss(MultipartFile file, String path) throws FileUploadException {
        File tmpFile = transferFile(file);
        assert tmpFile != null;
        double size = FileUtils.sizeOf(tmpFile) / unitSize;
        try {
            uploadFileToOss(path, tmpFile);
        } catch (IOException e) {
            throw new FileUploadException("===== 文件上传失败，原始文件信息，文件名称:" + file.getOriginalFilename() + ",文件大小:" + decimalFormat.format(size) + "KB");
        } finally {
            FileUtils.deleteQuietly(tmpFile);
        }
    }

    private void uploadFileToOss(String path, File tmpFile) throws JsonProcessingException {
        long start = System.currentTimeMillis();
        double size = FileUtils.sizeOf(tmpFile) / unitSize;
        String baseName = tmpFile.getName();
        if (log.isDebugEnabled()) {
            try {
                log.debug("======= 开始上传文件 原始文件信息，文件名称:{}，文件大小:{}KB，SRC64:{}", baseName, decimalFormat.format(size), FileUtils.checksum(tmpFile, new CRC64()).getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PutObjectResult putObjectResult = ossClient.putObject(aliyunOssProperties.getBucket(), path, tmpFile);
        if (log.isDebugEnabled()) {
            log.debug("======= 文件上传完成，耗费时间：{}ms,返回的信息：{}", System.currentTimeMillis() - start, objectMapper.writeValueAsString(putObjectResult));
        }
    }

    private File transferFile(MultipartFile file) {
        File tempFile = new File(FileUtils.getTempDirectoryPath() + "/" + file.getOriginalFilename());
        try {
            file.transferTo(tempFile);
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
