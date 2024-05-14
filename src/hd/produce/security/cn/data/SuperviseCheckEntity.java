/**
 * SuperviseCheckEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package hd.produce.security.cn.data;

import hd.utils.cn.StringUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * @Title: Entity
 * @Description: 监督抽查抽样信息
 * @author dxz
 * @date 2013-11-09 10:54:27
 * @version V1.0
 */
public class SuperviseCheckEntity implements java.io.Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 29510565718577056L;

    /** ID */
    private java.lang.String id;

    /** 样品编号 */
    private java.lang.String sampleCode;

    /** 注册商标 */
    private java.lang.String tradeMark;

    /** 包装 */
    private java.lang.String pack;

    /** 等级规格 */
    private java.lang.String specifications;

    /** 标识 */
    private java.lang.String flag;

    /** 生产日期或批号（检疫证号） */
    private java.lang.String batchNumber;

    /** 执行标准 */
    private java.lang.String execStandard;

    /** 产品认证登记情况 */
    private java.lang.String productCer;

    /** 产品认证登记证书编号 */
    private java.lang.String productCerNo;

    /** 获证日期 */
    private String certificateTime;

    /** 抽样数量 */
    private java.lang.String samplingCount;

    /** 抽样基数 */
    private java.lang.String samplingBaseCount;

    /** 通知书编号及有效期 */
    private java.lang.String noticeDetails;

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
     * @return: java.lang.String 注册商标
     */
    public java.lang.String getTradeMark() {
        return this.tradeMark;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 注册商标
     */
    public void setTradeMark(java.lang.String tradeMark) {
        this.tradeMark = tradeMark;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 包装
     */
    public java.lang.String getPack() {
        return this.pack;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 包装
     */
    public void setPack(java.lang.String pack) {
        this.pack = pack;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 等级规格
     */
    public java.lang.String getSpecifications() {
        return this.specifications;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 等级规格
     */
    public void setSpecifications(java.lang.String specifications) {
        this.specifications = specifications;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 标识
     */
    public java.lang.String getFlag() {
        return this.flag;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 标识
     */
    public void setFlag(java.lang.String flag) {
        this.flag = flag;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 生产日期或批号（检疫证号）
     */
    public java.lang.String getBatchNumber() {
        return this.batchNumber;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 生产日期或批号（检疫证号）
     */
    public void setBatchNumber(java.lang.String batchNumber) {
        this.batchNumber = batchNumber;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 执行标准
     */
    public java.lang.String getExecStandard() {
        return this.execStandard;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 执行标准
     */
    public void setExecStandard(java.lang.String execStandard) {
        this.execStandard = execStandard;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 产品认证登记情况
     */
    public java.lang.String getProductCer() {
        return this.productCer;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 产品认证登记情况
     */
    public void setProductCer(java.lang.String productCer) {
        this.productCer = productCer;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 产品认证登记证书编号
     */
    public java.lang.String getProductCerNo() {
        return this.productCerNo;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 产品认证登记证书编号
     */
    public void setProductCerNo(java.lang.String productCerNo) {
        this.productCerNo = productCerNo;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 获证日期
     */
    public String getCertificateTime() {
        return this.certificateTime;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 获证日期
     */
    public void setCertificateTime(String certificateTime) {
        this.certificateTime = certificateTime;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样数量
     */
    public java.lang.String getSamplingCount() {
        return this.samplingCount;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样数量
     */
    public void setSamplingCount(java.lang.String samplingCount) {
        this.samplingCount = samplingCount;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样基数
     */
    public java.lang.String getSamplingBaseCount() {
        return this.samplingBaseCount;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样基数
     */
    public void setSamplingBaseCount(java.lang.String samplingBaseCount) {
        this.samplingBaseCount = samplingBaseCount;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 通知书编号及有效期
     */
    public java.lang.String getNoticeDetails() {
        return this.noticeDetails;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 通知书编号及有效期
     */
    public void setNoticeDetails(java.lang.String noticeDetails) {
        this.noticeDetails = noticeDetails;
    }

    public void parseBySoapObject(SoapObject soapObject) {
        this.batchNumber = StringUtils.getStrProperty(soapObject, "batchNumber");
        this.certificateTime = StringUtils.getStrProperty(soapObject, "certificateTime");
        this.execStandard = StringUtils.getStrProperty(soapObject, "execStandard");
        this.flag = StringUtils.getStrProperty(soapObject, "flag");
        this.id = StringUtils.getStrProperty(soapObject, "id");
        this.noticeDetails = StringUtils.getStrProperty(soapObject, "noticeDetails");
        this.pack = StringUtils.getStrProperty(soapObject, "pack");
        this.productCer = StringUtils.getStrProperty(soapObject, "productCer");
        this.productCerNo = StringUtils.getStrProperty(soapObject, "productCerNo");
        this.sampleCode = StringUtils.getStrProperty(soapObject, "sampleCode");
        this.samplingBaseCount = StringUtils.getStrProperty(soapObject, "samplingBaseCount");
        this.samplingCount = StringUtils.getStrProperty(soapObject, "samplingCount");
        this.specifications = StringUtils.getStrProperty(soapObject, "specifications");
        this.tradeMark = StringUtils.getStrProperty(soapObject, "tradeMark");
    }
}
