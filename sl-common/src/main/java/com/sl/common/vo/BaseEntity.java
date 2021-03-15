package com.sl.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 状态1:启用，0禁用
     */
    private int status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人id
     */
    private Long createId;
    /**
     * 更新人id
     */
    private Long updateId;
}
