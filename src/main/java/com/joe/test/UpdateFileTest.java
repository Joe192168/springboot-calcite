package com.joe.test;

import com.joe.file.HttpsFileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UpdateFileTest {

    public static void main(String[] args) {
        HttpsFileUtils httpsUtils = new HttpsFileUtils();
        //要上传的文件的路径
        String filePath = "D:/git/fastPlat/commonWeb/src/com/pingan/file/data/base64_New2.jpg";
        String postUrl  = "http://localhost:8080/commonWeb/FileUploadServlet";
        Map<String,String> postParam = new HashMap<String,String>();
        postParam.put("custNo", "11122");
        File postFile = new File(filePath);
        Map<String,Object> resultMap = httpsUtils.uploadFileByHTTP(postFile,postUrl,postParam);
        System.out.println(resultMap);
    }
}
