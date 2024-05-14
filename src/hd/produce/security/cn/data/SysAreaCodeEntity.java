
package hd.produce.security.cn.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dxz
 * @version V1.0
 * @Title: MonitoringProjectEntity
 * @Description: 检测项目数据
 * @date 2013-10-30 13:50:57
 */
public class SysAreaCodeEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 地区名称
     */
    private String areaname;

    /**
     * 地区Code
     */
    private String code;

    /**
     * 市列表
     */
    private List<SysAreaCodeEntity> jsonCity;

    /**
     * 区列表
     */
    private List<SysAreaCodeEntity> jsonCounty;

    public SysAreaCodeEntity(String areaname, String code, List<SysAreaCodeEntity> jsonCity,
            List<SysAreaCodeEntity> jsonCounty) {
        super();
        this.areaname = areaname;
        this.code = code;
        this.jsonCity = jsonCity;
        this.jsonCounty = jsonCounty;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<SysAreaCodeEntity> getJsonCity() {
        return jsonCity;
    }

    public void setJsonCity(List<SysAreaCodeEntity> jsonCity) {
        this.jsonCity = jsonCity;
    }

    public List<SysAreaCodeEntity> getJsonCounty() {
        return jsonCounty;
    }

    public void setJsonCounty(List<SysAreaCodeEntity> jsonCounty) {
        this.jsonCounty = jsonCounty;
    }

}
