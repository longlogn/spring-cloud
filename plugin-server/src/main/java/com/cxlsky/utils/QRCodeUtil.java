package com.cxlsky.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CXL
 */
public class QRCodeUtil {
    /**
     * 默认二维码宽度
     */
    private static final int WIDTH = 300;
    /**
     * 默认二维码高度
     */
    private static final int HEIGHT = 300;
    /**
     * 默认二维码文件格式
     */
    private static final String FORMAT = "png";
    /**
     * 二维码参数
     */
    private static final Map<EncodeHintType, Object> HINTS = new HashMap<>();

    static {
        // 字符编码
        HINTS.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 二维码与图片边距
        HINTS.put(EncodeHintType.MARGIN, 1);
    }

    /**
     * 返回一个 BufferedImage 对象
     *
     * @param content 二维码内容
     * @param width   宽
     * @param height  高
     */
    public static BufferedImage toBufferedImage(String content, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }


    /**
     * 将二维码图片输出到一个流中
     *
     * @param content 二维码内容
     * @param stream  输出流
     * @param width   宽
     * @param height  高
     */
    public static void writeToStream(String content, OutputStream stream, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
        MatrixToImageWriter.writeToStream(bitMatrix, FORMAT, stream);
    }

    /**
     * 生成二维码图片文件
     *
     * @param content 二维码内容
     * @param path    文件保存路径
     * @param remark
     * @param width   宽
     * @param height  高
     */
    public static void createQRCode(String content, String remark, String path, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
        if (StringUtils.isNotEmpty(remark)) {
            BufferedImage bufferedImage = toBufferedImage(content, width, height);
            addTextAppendImage(remark, path, bufferedImage);
        } else {
            //toPath() 方法由 jdk1.7 及以上提供
            MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, new File(path).toPath());

        }

    }

    /**
     * 为图片添加文字
     */
    private static void addTextAppendImage(String productName, String path, BufferedImage image) {

        try {
            int rawWidth = image.getWidth();
            int rawHeight = image.getHeight();
            //新的图片,二维码下面加上文字
            BufferedImage textBim = new BufferedImage(rawWidth, rawHeight + 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D textBimGraphics = textBim.createGraphics();
            //画二维码到新的面板
            textBimGraphics.setColor(Color.WHITE);
            textBimGraphics.fillRect(0, 0, rawWidth, rawHeight + 200);
            textBimGraphics.setColor(Color.BLACK);
            textBimGraphics.drawImage(image, 0, 0, rawWidth, rawHeight, null);

            AffineTransform affinetransform = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

            Font mf = new Font("Source Han Sans CN", Font.BOLD, 30);
            //画文字到新的面板
            textBimGraphics.setColor(Color.BLACK);
            textBimGraphics.setFont(mf);
            FontMetrics fontMetrics = textBimGraphics.getFontMetrics();
            //消除文字锯齿
            textBimGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            //消除画图锯齿
            textBimGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = (rawWidth - fontMetrics.stringWidth(productName)) / 2;
            int y = textBim.getHeight() + (rawHeight - textBim.getHeight()) / 2;
            //画文字
            textBimGraphics.drawString(productName, x, y - 25);
            Font a = new Font("Source Han Sans CN", Font.BOLD, 20);

            image.flush();
            textBim.flush();
            ImageIO.write(textBim, FORMAT, new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
