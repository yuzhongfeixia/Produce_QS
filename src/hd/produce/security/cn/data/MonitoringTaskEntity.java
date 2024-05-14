
package hd.produce.security.cn.data;

import hd.produce.security.cn.annotation.Except;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.StringUtils;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

/**
 * @author dxz
 * @version V1.0
 * @Title: Entity
 * @Description: 监测任务表
 * @date 2013-11-06 17:46:47
 */
public class MonitoringTaskEntity implements java.io.Serializable {
    /**
     * 序列ID
     */
    private static final long serialVersionUID = 5027592506675366572L;
    /**
     * 编号
     */
    private java.lang.String id;
    /**
     * 项目编号
     */
    private java.lang.String projectCode;
    /**
     * 任务名称
     */
    private java.lang.String taskname;
    /**
     * 质检机构
     */
    private java.lang.String orgCode;
    /**
     * 行政区划
     */
    private java.lang.String areacode;
    /**
     * 监测环节
     */
    private java.lang.String monitoringLink;
    /**
     * 抽样品种
     */
    private java.lang.String agrCode;
    /**
     * 抽样数量
     */
    private java.lang.Integer samplingCount;
    /**
     * 任务编码
     */
    private java.lang.String taskcode;
    /**
     * 抽样数量 字符串类型
     */
    private java.lang.String samplingCounts;
    /**
     * 抽样单模板
     */
    private java.lang.String sampleTemplet;
    /**
     * 方案级别*
     */
    private java.lang.String plevel;
    /**
     * 监测类型*
     */
    private java.lang.String monitorType;
    
    /**
     * 任务详情的抽样地区(字符串)
     */
    private String sampleArea;
    
    /**
     * 抽样单的抽样品种 (list)
     */
    @Except
    private List<MonitoringBreedEntity> projectBreedList; 
    
    /**
     * 任务详情列表(bean)
     */
    @Except
    private MonitoringTaskDetailsEntity taskDetail;

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
     * @return: java.lang.String 项目编号
     */
    public String getProjectCode() {
        return this.projectCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 项目编号
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 任务名称
     */
    public String getTaskname() {
        return this.taskname;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 任务名称
     */
    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 质检机构
     */
    public String getOrgCode() {
        return this.orgCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 质检机构
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 行政区划
     */
    public String getAreacode() {
        return this.areacode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 行政区划
     */
    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 监测环节
     */
    public String getMonitoringLink() {
        return this.monitoringLink;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 监测环节
     */
    public void setMonitoringLink(String monitoringLink) {
        this.monitoringLink = monitoringLink;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 抽样品种
     */
    public String getAgrCode() {
        return this.agrCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 抽样品种
     */
    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 抽样数量
     */
    public Integer getSamplingCount() {
        return this.samplingCount;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer 抽样数量
     */
    public void setSamplingCount(Integer samplingCount) {
        this.samplingCount = samplingCount;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 任务编码
     */
    public String getTaskcode() {
        return taskcode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 任务编码
     */
    public void setTaskcode(String taskcode) {
        this.taskcode = taskcode;
    }

    public String getSamplingCounts() {
        return samplingCounts;
    }

    public void setSamplingCounts(String samplingCounts) {
        this.samplingCounts = samplingCounts;
    }

    public String getSampleTemplet() {
        return this.sampleTemplet;
    }

    public void setSampleTemplet(String sampleTemplet) {
        this.sampleTemplet = sampleTemplet;
    }

    public String getPlevel() {
        return plevel;
    }

    public void setPlevel(String plevel) {
        this.plevel = plevel;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public List<MonitoringBreedEntity> getProjectBreedList() {
        return projectBreedList;
    }

    public void setProjectBreedList(List<MonitoringBreedEntity> projectBreedList) {
        this.projectBreedList = projectBreedList;
    }

    public MonitoringTaskDetailsEntity getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(MonitoringTaskDetailsEntity taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getSampleArea() {
        return sampleArea;
    }

    public void setSampleArea(String sampleArea) {
        this.sampleArea = sampleArea;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        this.id = StringUtils.getStrProperty(soapObject, "id");
        this.agrCode = StringUtils.getStrProperty(soapObject, "agrCode");
        this.areacode = StringUtils.getStrProperty(soapObject, "areacode");
        this.monitoringLink = StringUtils.getStrProperty(soapObject, "monitoringLink");
        this.orgCode = StringUtils.getStrProperty(soapObject, "orgCode");
        this.plevel = StringUtils.getStrProperty(soapObject, "plevel");
        this.projectCode = StringUtils.getStrProperty(soapObject, "projectCode");
        this.samplingCount = StringUtils.getIntProperty(soapObject, "samplingCount");
        this.samplingCounts = StringUtils.getStrProperty(soapObject, "samplingCounts");
        this.taskcode = StringUtils.getStrProperty(soapObject, "taskcode");
        this.sampleTemplet = StringUtils.getStrProperty(soapObject, "sampleTemplet");
        this.taskname = StringUtils.getStrProperty(soapObject, "taskname");
        this.monitorType = StringUtils.getStrProperty(soapObject, "monitorType");
        this.sampleArea = StringUtils.getStrProperty(soapObject, "sampleArea");
        if (soapObject.hasProperty("taskDetail")) {
            MonitoringTaskDetailsEntity datail = new MonitoringTaskDetailsEntity();
            datail.parseBySoapObject((SoapObject) soapObject.getProperty("taskDetail"));
            taskDetail = datail;
        }
        if(soapObject.hasProperty("projectBreedList")){
            projectBreedList = new ArrayList<MonitoringBreedEntity>();
            for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                SoapObject soapBreed;
                try {
                    soapBreed = (SoapObject) soapObject.getProperty(i);
                } catch (Exception e) {
                    continue;
                }
                // 如果是任务详情则跳过
                try{
                    if(ConverterUtil.isNotEmpty(soapBreed.getProperty("taskCode"))){
                        continue;
                    }
                } catch (Exception e) {
                    MonitoringBreedEntity linkInfo = new MonitoringBreedEntity();
                    linkInfo.parseBySoapObject(soapBreed);
                    linkInfo.setProjectCode(this.projectCode);
                    projectBreedList.add(linkInfo);
                }
            }
        }
    }

}
