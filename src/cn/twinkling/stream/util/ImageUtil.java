package cn.twinkling.stream.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @from http://www.cnblogs.com/umasuo/archive/2012/05/28/java_image_inOut.html
 *       http://blog.csdn.net/a673341766/article/details/16827629
 * @author: 你看起来很好吃
 * @author: 天才少年程序员-李荣盛专栏
 * @Modified 2018-08-11
 */
public class ImageUtil {
    public static boolean scaleImage(String srcPath, String targetPath, int width, int height){
        try {
            BufferedImage bufferedImage = null;

            File of = new File(srcPath);
            if (of.canRead()) {
                bufferedImage = ImageIO.read(of);
            }

            BufferedImage newBufferedImage = new BufferedImage(width, height, bufferedImage.getType());
            Graphics g = newBufferedImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();

            FileOutputStream scaleImage = new FileOutputStream(targetPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(scaleImage);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(newBufferedImage);

            /* 压缩质量 */
            jep.setQuality(0.9f, true);
            encoder.encode(newBufferedImage, jep);
            scaleImage.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean scaleImageByPercent(String srcPath, String targetPath, float percent){
        try {
            BufferedImage bufferedImage = null;

            File of = new File(srcPath);
            if (of.canRead()) {
                bufferedImage = ImageIO.read(of);
            }

            return scaleImage(srcPath, targetPath, (int)(bufferedImage.getWidth()*percent), (int)(bufferedImage.getHeight()*percent));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
