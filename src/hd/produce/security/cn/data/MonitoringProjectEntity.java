
package hd.produce.security.cn.data;

import org.ksoap2.serialization.SoapObject;

import hd.utils.cn.StringUtils;

import java.io.Serializable;

/**
 * @author dxz
 * @version V1.0
 * @Title: MonitoringProjectEntity
 * @Description: 检测项目数据
 * @date 2013-10-30 13:50:57
 */
public class MonitoringProjectEntity implements Serializable {
    /**
     * 编号
     */
    private String id;

    /**
     * 方案ID
     */
    private String planCode;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 牵头单位编号
     */
    private String leadunit;

    /**
     * 监测开始时间
     */
    private String starttime;

    /**
     * 监测结束时间
     */
    private String endtime;

    /**
     * 是否抽检分离
     */
    private String detached;

    /**
     * 项目状态
     */
    private String state;

    /**
     * 项目ID
     */
    private String projectCode;

    /**
     * 发布时间
     */
    private String publishDate;

    /**
     * 抽样单模板
     */
    private String sampleTemplet;

    /**
     * 行业
     * 
     */
    private String industryCode;

    /**
     * 整拆包分发FLG
     */
    private Integer packingFlg;

    /**
     * 方案级别(检索结果用)*
     */
    private String plevel;

    /**
     * 起始发布时间(检索条件)
     */
    private String publishDate_begin;

    /**
     * 终了发布时间(检索条件)
     */
    private String publishDate_end;

    /**
     * 机构名称(检索条件)
     */
    private String ogrname;

    /**
     * 样品编码(检索条件)
     */
    private String spCode;

    /**
     * 条码(检索条件)
     */
    private String dCode;

    /**
     * 样品名称(检索条件)
     */
    private String sampleName;

    /**
     * 任务名称(检索条件)
     */
    private String taskName;

    /**
     * 监测类型
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getPadId() {
        return padId;
    }

    public void setPadId(String padId) {
        this.padId = padId;
    }

    /**
     * 客户端pad(检索条件)
     */
    private String padId;

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 编号
     */

    public String getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 编号
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 方案ID
     */
    public String getPlanCode() {
        return this.planCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 方案ID
     */
    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 项目名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 项目名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 牵头单位编号
     */
    public String getLeadunit() {
        return this.leadunit;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 牵头单位编号
     */
    public void setLeadunit(String leadunit) {
        this.leadunit = leadunit;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.lang.String 监测开始时间
     */
    public String getStarttime() {
        return this.starttime;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 监测开始时间
     */
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 监测结束时间
     */
    public String getEndtime() {
        return this.endtime;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 监测结束时间
     */
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 是否抽检分离
     */
    public String getDetached() {
        return this.detached;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 是否抽检分离
     */
    public void setDetached(String detached) {
        this.detached = detached;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 项目状态
     */
    public String getState() {
        return this.state;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 项目状态
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 项目ID
     */
    public String getProjectCode() {
        return this.projectCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 项目ID
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 发布时间
     */
    public String getPublishDate() {
        return this.publishDate;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 发布时间
     */
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样单模板
     */
    public String getSampleTemplet() {
        return this.sampleTemplet;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样单模板
     */
    public void setSampleTemplet(String sampleTemplet) {
        this.sampleTemplet = sampleTemplet;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 行业
     */
    public String getIndustryCode() {
        return this.industryCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 行业
     */
    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 整拆包分发FLG
     */
    public Integer getPackingFlg() {
        return this.packingFlg;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 整拆包分发FLG
     */
    public void setPackingFlg(Integer packingFlg) {
        this.packingFlg = packingFlg;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return java.lang.String 方案级别(检索结果用)*
     */
    public String getPlevel() {
        return plevel;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 方案级别(检索结果用)*
     */
    public void setPlevel(String plevel) {
        this.plevel = plevel;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @return: java.util.Date 起始发布时间(检索条件)
     */
    public String getPublishDate_begin() {
        return publishDate_begin;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 起始发布时间(检索条件)
     */
    public void setPublishDate_begin(String publishDate_begin) {
        this.publishDate_begin = publishDate_begin;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 终了发布时间(检索条件)
     */
    public String getPublishDate_end() {
        return publishDate_end;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 终了发布时间(检索条件)
     */
    public void setPublishDate_end(String publishDate_end) {
        this.publishDate_end = publishDate_end;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 机构名称(检索条件)
     */
    public String getOgrname() {
        return ogrname;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 机构名称(检索条件)
     */
    public void setOgrname(String ogrname) {
        this.ogrname = ogrname;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品编码(检索条件)
     */
    public String getSpCode() {
        return spCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.Integer 样品编码(检索条件)
     */
    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.String 条码(检索条件)
     */
    public String getDCode() {
        return dCode;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.String 条码(检索条件)
     */
    public void setDCode(String dCode) {
        this.dCode = dCode;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 样品名称(检索条件)
     */
    public String getSampleName() {
        return sampleName;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品名称(检索条件)
     */
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        this.id = StringUtils.getStrProperty(soapObject, "id");
        this.detached = StringUtils.getStrProperty(soapObject, "detached");
        this.endtime = StringUtils.getStrProperty(soapObject, "endtime");
        this.industryCode = StringUtils.getStrProperty(soapObject, "industryCode");
        this.leadunit = StringUtils.getStrProperty(soapObject, "leadunit");
        this.name = StringUtils.getStrProperty(soapObject, "name");
        this.ogrname = StringUtils.getStrProperty(soapObject, "ogrname");
        this.packingFlg = StringUtils.getIntProperty(soapObject, "packingFlg");
        this.planCode = StringUtils.getStrProperty(soapObject, "planCode");
        this.plevel = StringUtils.getStrProperty(soapObject, "plevel");
        this.projectCode = StringUtils.getStrProperty(soapObject, "projectCode");
        this.publishDate = StringUtils.getStrProperty(soapObject, "publishDate");
        this.publishDate_begin = StringUtils.getStrProperty(soapObject, "publishDate_begin");
        this.publishDate_end = StringUtils.getStrProperty(soapObject, "publishDate_end");
        this.sampleName = StringUtils.getStrProperty(soapObject, "sampleName");
        this.sampleTemplet = StringUtils.getStrProperty(soapObject, "sampleTemplet");
        this.spCode = StringUtils.getStrProperty(soapObject, "spCode");
        this.starttime = StringUtils.getStrProperty(soapObject, "starttime");
        this.state = StringUtils.getStrProperty(soapObject, "state");
        this.dCode = StringUtils.getStrProperty(soapObject, "dCode");
        this.taskName = StringUtils.getStrProperty(soapObject, "taskName");
        this.padId = StringUtils.getStrProperty(soapObject, "padId");
        this.type = StringUtils.getStrProperty(soapObject, "type");
    }

}
