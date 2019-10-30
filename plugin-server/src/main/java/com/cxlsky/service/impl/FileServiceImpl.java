package com.cxlsky.service.impl;

import com.cxlsky.bean.AliyunOssUploadClient;
import com.cxlsky.enums.FileTypeEnum;
import com.cxlsky.exception.FileUploadException;
import com.cxlsky.service.FileService;
import com.cxlsky.vo.UploadResultVO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author CXL
 */
@Service
public class FileServiceImpl implements FileService {

    private String[] imageExtensions = { "jpg", "png", "jpeg" };

    private String[] audioExtensions = { "mp3", "wav", "silk" };

    private String[] textExtensions = { "docx", "pdf", "doc" };

    @Autowired
    private AliyunOssUploadClient aliyunOssUploadClient;

    @Override
    public boolean isValid(FileTypeEnum fileType, MultipartFile multipartFile) {
        if (multipartFile == null) {
            return false;
        }
        String[] uploadExtensions;
        if (fileType == FileTypeEnum.AUDIO) {
            uploadExtensions = audioExtensions;
        } else if (fileType == FileTypeEnum.TEXT) {
            uploadExtensions = textExtensions;
        } else {
            uploadExtensions = imageExtensions;
        }
        if (ArrayUtils.isNotEmpty(uploadExtensions)) {
            String originalFilename = multipartFile.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)) {
                return false;
            }
            return FilenameUtils.isExtension(originalFilename.toLowerCase(), uploadExtensions);
        }
        return true;
    }

    @Override
    public UploadResultVO upload(MultipartFile multipartFile) {
        try {
            File tempFile = new File(FileUtils.getTempDirectoryPath() + "/" + multipartFile.getOriginalFilename());
            multipartFile.transferTo(tempFile);
            String path = aliyunOssUploadClient.uploadFile(tempFile, false);
            return new UploadResultVO(path, multipartFile.getSize(), multipartFile.getOriginalFilename(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new FileUploadException("文件上传失败");
    }

    @Override
    public String upload(File file) {
        return aliyunOssUploadClient.uploadFile(file, false);

    }
}
