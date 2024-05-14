
package hd.produce.security.cn.data;

import org.ksoap2.serialization.SoapObject;

import hd.utils.cn.StringUtils;

/**
 * @version V1.0
 * @Title: Entity
 * @Description: 客户端(PAD)
 * @date 2013-10-18 16:28:48
 */
public class MonitoringPadEntity implements java.io.Serializable {
    /**
     * 客户端ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属质检机构
     */
    private String orgCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 质检机构名 --检索条件
     */
    private String orgName;

    /**
     * 任务数量--检索结果
     */
    private Integer taskCount;

    /**
     * 成功标识
     */
    private String flg;

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 客户端ID
     */

    public String getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 客户端ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 用户名
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 密码
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 所属质检机构
     */
    public String getOrgCode() {
        return this.orgCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 所属质检机构
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        this.id = StringUtils.getStrProperty(soapObject, "id");
        this.orgCode = StringUtils.getStrProperty(soapObject, "orgCode");
        this.orgName = StringUtils.getStrProperty(soapObject, "orgName");
        this.password = StringUtils.getStrProperty(soapObject, "password");
        this.remark = StringUtils.getStrProperty(soapObject, "remark");
        this.username = StringUtils.getStrProperty(soapObject, "username");
        this.flg = StringUtils.getStrProperty(soapObject, "flg");
        this.taskCount = StringUtils.getIntProperty(soapObject, "taskCount");

    }

    public String getFlg() {
        return flg;
    }

    public void setFlg(String flg) {
        this.flg = flg;
    }

	@Override
	public String toString() {
		return "MonitoringPadEntity [id=" + id + ", username=" + username
				+ ", password=" + password + ", orgCode=" + orgCode
				+ ", remark=" + remark + ", orgName=" + orgName
				+ ", taskCount=" + taskCount + ", flg=" + flg + "]";
	}

}
