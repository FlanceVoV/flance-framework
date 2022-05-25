package com.flance.components.jobs.domain.job.entity;

import com.flance.jdbc.mybatis.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysJob extends BaseEntity<String> {
}
