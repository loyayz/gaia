package com.loyayz.gaia.data.mybatis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;

import java.util.Date;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class AbstractTable<T extends BaseTable> extends BaseTable<T> {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    /**
     * 生成 id
     */
    public Long genId() {
        this.id = (long) MybatisUtils.getIdentifierGenerator(getClass()).nextId(this);
        return this.id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

}
