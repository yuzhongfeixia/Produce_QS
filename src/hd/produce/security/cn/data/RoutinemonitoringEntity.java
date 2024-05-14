/**
 * RoutinemonitoringEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package hd.produce.security.cn.data;

import hd.produce.security.cn.annotation.Except;
import hd.utils.cn.StringUtils;

import java.util.Date;

import org.ksoap2.serialization.SoapObject;

/**
 * @Title: Entity
 * @Description: 例行监测信息表
 * @author dxz
 * @date 2013-11-06 19:12:13
 * @version V1.0
 */
public class RoutinemonitoringEntity implements java.io.Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 6264994359502647494L;

    /** ID */
    private java.lang.String id;

    /** 样品编号 */
    private java.lang.String sampleCode;

    /** 样品来源 */
    private java.lang.String sampleSource;

    /** 样品量(n/N) */
    private java.lang.String sampleCount;

    /** 任务来源 */
    private java.lang.String taskSource;

    /** 执行标准 */
    private java.lang.String execStanderd;

    /** 抽样单ID */
    private String samplingMonadId;

    /** 备注 */
    @Except
    private java.lang.String remark;

    /** 条码 **/
    @Except
    private java.lang.String dCode;

    /** 农产品编码 */
    @Except
    private java.lang.String agrCode;

    /** 样品名称(回显用) */
    @Except
    private java.lang.String sampleName;

    /** 图片-主表ID(上次时用于对应主表ID) */
    @Except
    private String imgContent;
    @Except
    private String samplePath;

    /**
     * 抽样详细地址
     */
    @Except
    private String samplingAddress;

    /**
     * 抽样单创建时间
     */
    @Except
    private String samplingTime;

    /**
     * 任务Code
     * 
     */
    @Except
    private String taskCode;

    public java.lang.String getSampleName() {
        return sampleName;
    }

    public void setSampleName(java.lang.String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSamplingAddress() {
        return samplingAddress;
    }

    public void setSamplingAddress(String samplingAddress) {
        this.samplingAddress = samplingAddress;
    }

    public String getSamplePath() {
        return samplePath;
    }

    public void setSamplePath(String samplePath) {
        this.samplePath = samplePath;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String ID
     */
    public java.lang.String getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String ID
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品编号
     */
    public java.lang.String getSampleCode() {
        return this.sampleCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品编号
     */
    public void setSampleCode(java.lang.String sampleCode) {
        this.sampleCode = sampleCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品来源
     */
    public java.lang.String getSampleSource() {
        return this.sampleSource;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品来源
     */
    public void setSampleSource(java.lang.String sampleSource) {
        this.sampleSource = sampleSource;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品量(n/N)
     */
    public java.lang.String getSampleCount() {
        return this.sampleCount;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品量(n/N)
     */
    public void setSampleCount(java.lang.String sampleCount) {
        this.sampleCount = sampleCount;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 任务来源
     */
    public java.lang.String getTaskSource() {
        return this.taskSource;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 任务来源
     */
    public void setTaskSource(java.lang.String taskSource) {
        this.taskSource = taskSource;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 执行标准
     */
    public java.lang.String getExecStanderd() {
        return this.execStanderd;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 执行标准
     */
    public void setExecStanderd(java.lang.String execStanderd) {
        this.execStanderd = execStanderd;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    public java.lang.String getRemark() {
        return this.remark;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 备注
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

    public java.lang.String getdCode() {
        return dCode;
    }

    public void setdCode(java.lang.String dCode) {
        this.dCode = dCode;
    }

    public java.lang.String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(java.lang.String agrCode) {
        this.agrCode = agrCode;
    }

    /**
     * 方法: 设置String
     * 
     * @param: String 设置图片-主表ID
     */
    public void setImgContent(String imgContent) {
        this.imgContent = imgContent;
    }

    /**
     * 方法: 取得String
     * 
     * @return: String 取得图片-主表ID
     */
    public String getImgContent() {
        return imgContent;
    }

    public String getSamplingMonadId() {
        return samplingMonadId;
    }

    public void setSamplingMonadId(String samplingMonadId) {
        this.samplingMonadId = samplingMonadId;
    }

    public String getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        try {
            this.agrCode = StringUtils.getStrProperty(soapObject, "agrCode");
            this.execStanderd = StringUtils.getStrProperty(soapObject, "execStanderd");
            this.id = StringUtils.getStrProperty(soapObject, "id");
            this.remark = StringUtils.getStrProperty(soapObject, "remark");
            this.sampleCode = StringUtils.getStrProperty(soapObject, "sampleCode");
            this.sampleCount = StringUtils.getStrProperty(soapObject, "sampleCount");
            this.sampleSource = StringUtils.getStrProperty(soapObject, "sampleSource");
            this.taskSource = StringUtils.getStrProperty(soapObject, "taskSource");
            this.dCode = StringUtils.getStrProperty(soapObject, "dCode");
            this.imgContent = StringUtils.getStrProperty(soapObject, "imgContent");
            this.samplingAddress = StringUtils.getStrProperty(soapObject, "samplingAddress");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
