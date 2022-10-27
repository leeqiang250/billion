package com.billion.framework.util;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class UploadImage {

    public static String upload(String imagePath) {
        File file = new File(imagePath);
        if (!file.exists()) {
            return "";
        }
        String[] cmds = new String[]{"curl", "-X", "POST", "-F", "file=@" + imagePath, "-H", "Authorization: Bearer 5_crM9D0ZQEjTmJm6P_J9CjAgxU06AKt0ZB-xeAb", "https://api.cloudflare.com/client/v4/accounts/06d48301c855502bf143d5a4c5d3a982/images/v1"};
        try {
            ProcessBuilder process = new ProcessBuilder(cmds);
            Process doStart = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(doStart.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            if (builder.length() == 0) {
                log.warn("nft_cloudflare info upload result is empty");
                return "";
            }
            Map map =JSONObject.parseObject(builder.toString(), Map.class);

            if (CollectionUtils.isEmpty(map)) {
                log.error("nft_cloudflare info get upload result is error {}", builder.toString());
                return "";
            }
            boolean success = (boolean) map.get("success");
//            FileOperateUtil.delFile(imagePath);
            if (success) {
                Map result = (Map) map.get("result");
                ArrayList<String> variants = (ArrayList<String>) result.get("variants");
                String newImageLink = variants.get(0);
                return newImageLink;
            }
            return "";
        } catch (IOException e) {
            log.error("上传图片异常:", e);
            return "";
        }
    }

    public static void main(String[] args) {
        String path = "/Users/renjian/Desktop/public.png";
        System.out.printf(upload(path));
    }
}
