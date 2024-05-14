package hd.produce.security.cn.data;

import org.ksoap2.serialization.SoapObject;

import hd.utils.cn.ConverterUtil;
import hd.utils.cn.StringUtils;

/**
 * @version V1.0
 * @Title: Entity
 * @Description: 抽样品种
 * @date 2013-10-18 16:28:48
 */
public class MonitoringBreedEntity implements java.io.Serializable {
    /**
     * 序列ID
     */
    private static final long serialVersionUID = -7458824668294784727L;
    
    // 项目品种(任务详细显示)
    public static final String TYPE_TASK = "0";
    // 抽样单品种
    public static final String TYPE_SAMPLE = "1";

    /**
     * ID
     */
    private String id;

    /**
     * 农产品code
     */
    private String agrCode;

    /**
     * 农产品名称
     */
    private String agrName;

    /**
     * 项目ID
     */
    private String projectCode;

    /**
     * 父ID
     */
    private String pId;

    public String getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String ID
     */
    public void setId(String id) {
        this.id = id;
    }


    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getAgrName() {
        return agrName;
    }

    public void setAgrName(String agrName) {
        this.agrName = agrName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        this.id = StringUtils.getStrProperty(soapObject, "id");
        if(ConverterUtil.isEmpty(this.id)){
            this.id = ConverterUtil.getUUID();
        }
        this.pId = StringUtils.getStrProperty(soapObject, "pId");
        this.projectCode = StringUtils.getStrProperty(soapObject, "projectCode");
        this.agrName = StringUtils.getStrProperty(soapObject, "agrName");
        this.agrCode = StringUtils.getStrProperty(soapObject, "agrCode");
    }

}
