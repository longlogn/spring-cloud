package com.cxlsky.controller;

import com.cxlsky.bean.AliyunOssUploadClient;
import com.cxlsky.enums.FileTypeEnum;
import com.cxlsky.service.FileService;
import com.cxlsky.utils.response.Body;
import com.cxlsky.utils.response.ResponseUtil;
import com.cxlsky.vo.UploadResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: FileController
 * @Description:
 * @author: Dean
 * @date: 2018/8/24
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("上传指定类型")
    @PostMapping("/upload/{fileType}")
    public ResponseEntity<Body<UploadResultVO>> upload(MultipartFile multipartFile, @PathVariable("fileType") FileTypeEnum fileType, boolean async) {
        if (!fileService.isValid(fileType, multipartFile)) {
            return ResponseUtil.badRequest("文件类型不正确");
        }
        return ResponseUtil.ok(fileService.upload(multipartFile));
    }

    @ApiOperation("上传所有类型")
    @PostMapping("/upload")
    public ResponseEntity<Body<UploadResultVO>> uploadAllType(MultipartFile multipartFile) {
        return ResponseUtil.ok(fileService.upload(multipartFile));
    }


}
