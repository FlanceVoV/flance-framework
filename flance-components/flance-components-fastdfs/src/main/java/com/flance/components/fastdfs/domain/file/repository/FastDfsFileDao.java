package com.flance.components.fastdfs.domain.file.repository;

import com.flance.components.fastdfs.domain.file.model.po.FastDfsFile;
import com.flance.jdbc.jpa.dao.BaseDao;
import org.springframework.stereotype.Repository;


@Repository
public interface FastDfsFileDao extends BaseDao<FastDfsFile, Long> {
}
