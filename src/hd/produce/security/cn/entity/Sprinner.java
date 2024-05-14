package hd.produce.security.cn.entity;

import java.io.Serializable;

import hd.utils.cn.ConverterUtil;

/**
 * 下拉框控件bean
 * 
 * @author xudl(Wisea)
 * 
 */
public class Sprinner implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 988572818780081237L;
    // 值
    private String id;
    // 名称
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (ConverterUtil.isEmpty(id)) {
            throw new RuntimeException("Error in Sprinner: id must be not null!");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
