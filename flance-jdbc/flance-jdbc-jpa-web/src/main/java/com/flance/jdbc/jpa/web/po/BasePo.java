package com.flance.jdbc.jpa.web.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 * 逻辑删除：
 * @SQLDelete(sql = "update yc_ytjg_enterprise_car set deleted = 1 where id = ? ")
 * @Where(clause = "deleted = 0")
 * @author jhf
 * @param <ID>
 */
@Data
@MappedSuperclass
public class BasePo<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = -3315045018004505703L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private ID id;

    @JsonIgnore
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    @JsonIgnore
    private String lastUpdateUserId;

    @JsonIgnore
    private String lastUpdateUserName;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String createUserId;

    private String createUserName;

    @JsonIgnore
    private Short deleted;

}
