package hd.produce.security.cn.data;

/**
 * @author dxz
 * @version V1.0
 * @Title: Entity
 * @Description: 项目质检机构关系表
 * @date 2013-10-30 13:50:54
 */
public class MonitoringOrganizationEntity implements java.io.Serializable {
    /**
     * ID
     */
    private String id;
    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 质检机构
     */
    private String orgCode;

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  ID
     */

    public String getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  项目编号
     */
    public String getProjectCode() {
        return this.projectCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  项目编号
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  质检机构
     */
    public String getOrgCode() {
        return this.orgCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  质检机构
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }


}
