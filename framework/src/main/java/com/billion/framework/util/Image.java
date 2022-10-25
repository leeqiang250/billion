package com.billion.framework.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author liqiang
 */
@Slf4j
public class Image {

    public static BufferedImage getBufferedImage(String file) throws IOException {
        InputStream stream = new FileInputStream(file);
        BufferedImage bi = ImageIO.read(stream);
        if (bi == null) {
            stream.close();
        }
        return bi;
    }

    public static void overlyingImage(BufferedImage bufferedImage, BufferedImage bufferedImage1, int x, int y, float alpha) throws IOException {
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = bufferedImage.createGraphics();
        // 获取层图的宽度
        int waterImgWidth = bufferedImage1.getWidth();
        // 获取层图的高度
        int waterImgHeight = bufferedImage1.getHeight();
        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 绘制
        g2d.drawImage(bufferedImage1, x, y, waterImgWidth, waterImgHeight, null);
        // 释放图形上下文使用的系统资源
        g2d.dispose();
    }

    public static boolean saveFile(BufferedImage bufferedImage, String path) {
        int temp = path.lastIndexOf(".") + 1;
        try {
            File outFile = new File(path);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            ImageIO.write(bufferedImage, path.substring(temp), outFile);
            return true;
        } catch (IOException e) {
            log.error("{}", e);
            return false;
        }
    }

    public static boolean mergePicture(ArrayList<String> paths, String path) {
        BufferedImage bufferedImage = null;
        try {
            for (String pic : paths) {
                log.info("{}", pic);
                if (Objects.isNull(bufferedImage)) {
                    bufferedImage = getBufferedImage(pic);
                } else {
                    overlyingImage(bufferedImage, getBufferedImage(pic), 0, 0, 1.0f);
                }
            }

            return saveFile(bufferedImage, path);
        } catch (IOException e) {
            log.error("{}", e);
            return false;
        }
    }

}