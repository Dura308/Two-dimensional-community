package com.zlee.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author z-Lee
 * @date 2023-01-16-23:11
 */
public class FtpUtil {

    private static final String PICTURE_FOLDER = "http://47.109.51.114:8089/picture/";

    public static String uploadImageByInputStream(String userId, MultipartFile file) throws IOException {
        //创建ftp客户端
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        //服务器地址
        String hostname = "47.109.51.114";
        //ftp端口
        int port = 21;
        //ftp用户名
        String username = "zlee_tofu_picture";
        //密码
        String password = "j8dySB6NY7TYK25S";

        //连接ftp服务器
        ftpClient.connect(hostname, port);
        //登陆ftp
        ftpClient.login(username, password);
        int reply = ftpClient.getReplyCode();
        System.out.println(reply);
        //如果reply返回230就算成功了，如果返回530密码用户名错误或当前用户无权限
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new RuntimeException("ftp用户名或密码错误或当前用户无权限");
        }
        //设置文件类型(二进制)
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        //设置被动模式
        ftpClient.enterLocalPassiveMode();

        //ftpClient.makeDirectory("images");//在root目录下创建文件夹
        //String remoteFileName = System.currentTimeMillis() + "_" + imageName;
        //ftpClient.storeFile("/www/wwwroot/vftp/" + imageName, is);//文件你若是不指定就会上传到root目录下
        //文件你若是不指定就会上传到root目录下

        ftpClient.makeDirectory(userId);
        InputStream fileInputStream = file.getInputStream();
        ftpClient.storeFile("/" + userId + "/" + file.getOriginalFilename(), fileInputStream);
        fileInputStream.close();
        ftpClient.logout();
        ftpClient.disconnect();
        return PICTURE_FOLDER + userId + "/" + file.getOriginalFilename();
    }
}

