package com.flance.components.fastdfs.infrastructure.utils;

import com.flance.components.fastdfs.domain.file.model.dto.FastDfsFileDto;
import com.flance.components.fastdfs.infrastructure.exception.FastDfsException;
import com.flance.components.fastdfs.infrastructure.fastdfs.FastDfsStorage;
import com.flance.web.utils.AssertException;
import com.flance.web.utils.AssertUtil;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

@Component
public class FastDfsClient {

    private final Logger logger = LoggerFactory.getLogger(FastDfsClient.class);

    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问地址
     */
    @FastDfsStorage(clientArgIndex = 1)
    public FastDfsFileDto uploadFile(MultipartFile file, StorageClient storageClient) {
        AssertUtil.notNull(storageClient, AssertException.getByEnum(AssertException.ErrCode.SYS_ERROR));
        FastDfsFileDto fastDfsFileDto = new FastDfsFileDto();
        fastDfsFileDto.setFileName(file.getOriginalFilename());
        fastDfsFileDto.setFileSize(file.getSize());
        fastDfsFileDto.setCreateDate(new Date());
        try {
            String[] results = storageClient.upload_file(file.getBytes(), "", null);
            String group = results[0];
            String fileName = results[1];
            fastDfsFileDto.setGroupName(group);
            fastDfsFileDto.setFileRealName(fileName);
            fastDfsFileDto.setFilePath(group + File.separator + fileName);
            fastDfsFileDto.setFileMd5(getMd5(file));
        } catch (IOException e) {
            e.printStackTrace();
            throw new FastDfsException(FastDfsException.FILE_BYTES_READ_ERR);
        } catch (MyException e) {
            e.printStackTrace();
            throw new FastDfsException(FastDfsException.FAST_DFS_UPLOAD_ERR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FastDfsException();
        }
        return fastDfsFileDto;
    }

    @FastDfsStorage(clientArgIndex = 1)
    public void downloadFile(FastDfsFileDto fastDfsFileDto, StorageClient storageClient, HttpServletResponse response) {
        try {
            byte[] bytes = storageClient.download_file(fastDfsFileDto.getGroupName(), fastDfsFileDto.getFileRealName());
            response.setHeader("Content-disposition", "attachment;filename=" + fastDfsFileDto.getFileName());
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (MyException | IOException e) {
            e.printStackTrace();
            throw new FastDfsException();
        } finally {
            try {
                response.getOutputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getMd5(MultipartFile file) {
        try {
            //获取文件的byte信息
            byte[] uploadBytes = file.getBytes();
            // 拿到一个MD5转换器
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            //转换为16进制
            return new BigInteger(1, digest).toString(16);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
