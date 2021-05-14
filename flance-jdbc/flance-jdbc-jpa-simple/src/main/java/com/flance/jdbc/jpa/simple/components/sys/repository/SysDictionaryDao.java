package com.flance.jdbc.jpa.simple.components.sys.repository;


import com.flance.jdbc.jpa.simple.components.sys.entity.SysDictionary;
import com.flance.jdbc.jpa.simple.dao.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * 系统字典dao
 * @author jhf
 */
@Repository
public interface SysDictionaryDao extends BaseRepository<SysDictionary, Long> {

}
