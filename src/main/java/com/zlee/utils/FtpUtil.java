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
    private static final String VIDEO_FOLDER = "http://47.109.51.114:8089/video/";
    private static final String AVATAR_FOLDER = "http://47.109.51.114:8089/avatar/";

    private static FTPClient connectFtp(String username, String password) throws IOException{
        //创建ftp客户端
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        //服务器地址
        String hostname = "47.109.51.114";
        //ftp端口
        int port = 21;

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

        return ftpClient;
    }

    public static String uploadImage(String userId, MultipartFile file) throws IOException {
        FTPClient ftpClient = connectFtp("zlee_tofu_picture", "j8dySB6NY7TYK25S");
        //ftpClient.makeDirectory("images");//在root目录下创建文件夹
        //String remoteFileName = System.currentTimeMillis() + "_" + imageName;
        //ftpClient.storeFile("/www/wwwroot/vftp/" + imageName, is);//文件你若是不指定就会上传到root目录下
        //文件你若是不指定就会上传到root目录下
        return uploadByInputStream(userId, file, ftpClient, PICTURE_FOLDER);
    }

    public static String uploadVideo(String userId, MultipartFile file) throws IOException {
        FTPClient ftpClient = connectFtp("zlee_tofu_video", "lz020308");
        //ftpClient.makeDirectory("images");//在root目录下创建文件夹
        //String remoteFileName = System.currentTimeMillis() + "_" + imageName;
        //ftpClient.storeFile("/www/wwwroot/vftp/" + imageName, is);//文件你若是不指定就会上传到root目录下
        //文件你若是不指定就会上传到root目录下
        return uploadByInputStream(userId, file, ftpClient, VIDEO_FOLDER);
    }

    private static String uploadByInputStream(String userId, MultipartFile file, FTPClient ftpClient, String folder) throws IOException {
        ftpClient.makeDirectory(userId);
        InputStream fileInputStream = file.getInputStream();
        ftpClient.storeFile("/" + userId + "/" + file.getOriginalFilename(), fileInputStream);
        fileInputStream.close();
        ftpClient.logout();
        ftpClient.disconnect();
        return folder + userId + "/" + file.getOriginalFilename();
    }

    public static String uploadAvatarByInputStream(MultipartFile file) throws IOException {

        FTPClient ftpClient = connectFtp("zlee_tofu", "lz020308");
        //ftpClient.makeDirectory("images");//在root目录下创建文件夹
        //String remoteFileName = System.currentTimeMillis() + "_" + imageName;
        //ftpClient.storeFile("/www/wwwroot/vftp/" + imageName, is);//文件你若是不指定就会上传到root目录下
        //文件你若是不指定就会上传到root目录下

        InputStream fileInputStream = file.getInputStream();
        boolean b = ftpClient.storeFile(file.getOriginalFilename(), fileInputStream);
        System.out.println(b);
        fileInputStream.close();
        ftpClient.logout();
        ftpClient.disconnect();
        return AVATAR_FOLDER + file.getOriginalFilename();
    }
}

