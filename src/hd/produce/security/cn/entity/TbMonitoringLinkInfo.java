package hd.produce.security.cn.entity;

import hd.utils.cn.ConverterUtil;
import hd.utils.cn.StringUtils;

import java.io.Serializable;

import org.ksoap2.serialization.SoapObject;

/**
 * 抽样环节表
 * 
 * @author xudl(Wisea)
 * 
 */
public class TbMonitoringLinkInfo implements Serializable {

    /**
     * 序列ID
     */
    private static final long serialVersionUID = 3968706978916750855L;
    private String id;
    private String projectId;
    private String code;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        this.id = ConverterUtil.getUUID();
        this.code = StringUtils.getStrProperty(soapObject, "typecode");
        this.name = StringUtils.getStrProperty(soapObject, "typename");
    }

}
