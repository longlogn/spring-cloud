package com.cxlsky.controller;

import com.cxlsky.service.FileService;
import com.cxlsky.utils.QRCodeUtil;
import com.cxlsky.vo.QRCodeInfo;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * @author CXL
 */
@RestController
@Slf4j
public class QRCodeController {

    @Autowired
    private FileService fileService;

    @PostMapping("/qr_codes")
    public QRCodeInfo createQrCode(@RequestBody QRCodeInfo qrCodeInfo) {
        try {
            log.info("================ qrcodeInfo : {}",qrCodeInfo.toString());
            String path = FileUtils.getTempDirectoryPath() + "/" + System.currentTimeMillis() + ".png";
            QRCodeUtil.createQRCode(qrCodeInfo.getMessage(), qrCodeInfo.getRemark(), path, qrCodeInfo.getWidth(), qrCodeInfo.getHeight());
            String uploadPath = fileService.upload(new File((path)));
            qrCodeInfo.setUrl(uploadPath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return qrCodeInfo;
    }

}
