package com.cxlsky.vo;


import com.cxlsky.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: UploadResultDto
 * @Description:
 * @author: Dean
 * @date: 2018/8/23
 */
@Data
@AllArgsConstructor
public class UploadResultVO {

    /**
     * 上传资源请求url
     */
    private String urlPath;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private FileTypeEnum fileTypeEnum;
}
