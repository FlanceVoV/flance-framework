package com.flance.components.fastdfs.infrastructure.exception;

public class FastDfsException extends RuntimeException {

    public final static String FILE_BYTES_READ_ERR = "文件byte[]读取失败！";

    public final static String FAST_DFS_UPLOAD_ERR = "fastDfs上传失败！";

    public final static String UNKOWN_ERR = "未知异常！";


    public FastDfsException() {
        super(UNKOWN_ERR);
    }

    public FastDfsException(String message) {
        super(message);
    }



}
