package com.cxlsky.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QRCodeInfo implements Serializable {
    private String url;
    /**
     * 至少设置为800*800
     */
    private int width;
    private int height;
    /**
     * 二维码解析内容
     */
    private String message;
    /**
     * 二维码图片下方备注信息，长度不超过40
     */
    private String remark;
}
