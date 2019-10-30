package com.cxlsky.service;

import com.cxlsky.enums.FileTypeEnum;
import com.cxlsky.vo.UploadResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @ClassName: IFileService
 * @Description:
 * @author: Dean
 * @date: 2018/8/23
 */
public interface FileService {

//    /**
//     * 文件上传
//     *
//     * @param fileType
//     *            文件类型
//     * @param multipartFile
//     *            上传文件
//     * @param async
//     *            是否异步
//     * @return 访问URL
//     */
//    UploadResultVO upload(FileTypeEnum fileType, MultipartFile multipartFile, boolean async);
//
//    /**
//     * 文件上传(异步)
//     *
//     * @param fileType
//     *            文件类型
//     * @param multipartFile
//     *            上传文件
//     * @return 上传结果
//     */
//    UploadResultVO upload(FileTypeEnum fileType, MultipartFile multipartFile);

    /**
     * 文件验证
     * 
     * @param fileType
     *            文件类型
     * @param multipartFile
     *            上传文件
     * @return
     */
    boolean isValid(FileTypeEnum fileType, MultipartFile multipartFile);

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     */
    UploadResultVO upload(MultipartFile multipartFile);

    /**
     *
     * @param file
     * @return
     */
    String upload(File file);
}
