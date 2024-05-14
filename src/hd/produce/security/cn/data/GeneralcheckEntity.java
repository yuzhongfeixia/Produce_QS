/**
 * GeneralcheckEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package hd.produce.security.cn.data;

import hd.utils.cn.StringUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * @Title: Entity
 * @Description: 普查样品信息
 * @author dxz
 * @date 2013-11-08 16:27:28
 * @version V1.0
 * 
 */
public class GeneralcheckEntity implements java.io.Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -1530023368050886758L;

    /** ID */
    private java.lang.String id;

    /** 样品编号 */
    private java.lang.String sampleCode;

    /** 商标 */
    private java.lang.String tradeMark;

    /** 包装 */
    private java.lang.String pack;

    /** 规格 */
    private java.lang.String specifications;

    /** 标识 */
    private java.lang.String flag;

    /** 执行标准 */
    private java.lang.String execStandard;

    /** 生产日期或批号 */
    private java.lang.String batchNumber;

    /** 产品认证情况 */
    private java.lang.String productCer;

    /** 证书编号 */
    private java.lang.String productCerNo;

    /** 抽样数量 */
    private java.lang.String samplingCount;

    /** 受检人与摊位号 */
    private java.lang.String stall;

    /** 电话 */
    private java.lang.String telphone;

    /** 传真 */
    private java.lang.String fax;

    /** 单位性质(生产) */
    private java.lang.String unitProperties;

    /** 单位名称(生产) */
    private java.lang.String unitName;

    /** 通讯地址(生产) */
    private java.lang.String unitAddress;

    /** 邮编(生产) */
    private java.lang.String zipCode;

    /** 法定代表人(生产) */
    private java.lang.String legalPerson;

    /** 联系人(生产) */
    private java.lang.String contacts;

    /** 电话(生产) */
    private java.lang.String telphone2;

    /** 传真(生产) */
    private java.lang.String fax2;

    /** 抽样文件编号 */
    private java.lang.String samplingNo;

    /** 等级 */
    private java.lang.String grade;

    /** 抽样基数 */
    private java.lang.String samplingCardinal;

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
     * @return: java.lang.String 商标
     */
    public java.lang.String getTradeMark() {
        return this.tradeMark;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 商标
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
     * @return: java.lang.String 规格
     */
    public java.lang.String getSpecifications() {
        return this.specifications;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 规格
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
     * @return: java.lang.String 生产日期或批号
     */
    public java.lang.String getBatchNumber() {
        return this.batchNumber;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 生产日期或批号
     */
    public void setBatchNumber(java.lang.String batchNumber) {
        this.batchNumber = batchNumber;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 产品认证情况
     */
    public java.lang.String getProductCer() {
        return this.productCer;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 产品认证情况
     */
    public void setProductCer(java.lang.String productCer) {
        this.productCer = productCer;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 证书编号
     */
    public java.lang.String getProductCerNo() {
        return this.productCerNo;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 证书编号
     */
    public void setProductCerNo(java.lang.String productCerNo) {
        this.productCerNo = productCerNo;
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
     * @return: java.lang.String 受检人与摊位号
     */
    public java.lang.String getStall() {
        return this.stall;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 受检人与摊位号
     */
    public void setStall(java.lang.String stall) {
        this.stall = stall;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 电话
     */
    public java.lang.String getTelphone() {
        return this.telphone;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 电话
     */
    public void setTelphone(java.lang.String telphone) {
        this.telphone = telphone;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 传真
     */
    public java.lang.String getFax() {
        return this.fax;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 传真
     */
    public void setFax(java.lang.String fax) {
        this.fax = fax;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位性质(生产)
     */
    public java.lang.String getUnitProperties() {
        return this.unitProperties;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 单位性质(生产)
     */
    public void setUnitProperties(java.lang.String unitProperties) {
        this.unitProperties = unitProperties;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位名称(生产)
     */
    public java.lang.String getUnitName() {
        return this.unitName;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 单位名称(生产)
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 通讯地址(生产)
     */
    public java.lang.String getUnitAddress() {
        return this.unitAddress;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 通讯地址(生产)
     */
    public void setUnitAddress(java.lang.String unitAddress) {
        this.unitAddress = unitAddress;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 邮编(生产)
     */
    public java.lang.String getZipCode() {
        return this.zipCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 邮编(生产)
     */
    public void setZipCode(java.lang.String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 法定代表人(生产)
     */
    public java.lang.String getLegalPerson() {
        return this.legalPerson;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 法定代表人(生产)
     */
    public void setLegalPerson(java.lang.String legalPerson) {
        this.legalPerson = legalPerson;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 联系人(生产)
     */
    public java.lang.String getContacts() {
        return this.contacts;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 联系人(生产)
     */
    public void setContacts(java.lang.String contacts) {
        this.contacts = contacts;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 电话(生产)
     */
    public java.lang.String getTelphone2() {
        return this.telphone2;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 电话(生产)
     */
    public void setTelphone2(java.lang.String telphone2) {
        this.telphone2 = telphone2;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 传真(生产)
     */
    public java.lang.String getFax2() {
        return this.fax2;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 传真(生产)
     */
    public void setFax2(java.lang.String fax2) {
        this.fax2 = fax2;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样文件编号
     */
    public java.lang.String getSamplingNo() {
        return this.samplingNo;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样文件编号
     */
    public void setSamplingNo(java.lang.String samplingNo) {
        this.samplingNo = samplingNo;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 等级
     */
    public java.lang.String getGrade() {
        return this.grade;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 等级
     */
    public void setGrade(java.lang.String grade) {
        this.grade = grade;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样基数
     */
    public java.lang.String getSamplingCardinal() {
        return this.samplingCardinal;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样基数
     */
    public void setSamplingCardinal(java.lang.String samplingCardinal) {
        this.samplingCardinal = samplingCardinal;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        this.batchNumber = StringUtils.getStrProperty(soapObject, "batchNumber");
        this.contacts = StringUtils.getStrProperty(soapObject, "contacts");
        this.execStandard = StringUtils.getStrProperty(soapObject, "execStandard");
        this.fax = StringUtils.getStrProperty(soapObject, "fax");
        this.fax2 = StringUtils.getStrProperty(soapObject, "fax2");
        this.flag = StringUtils.getStrProperty(soapObject, "flag");
        this.grade = StringUtils.getStrProperty(soapObject, "grade");
        this.id = StringUtils.getStrProperty(soapObject, "id");
        this.legalPerson = StringUtils.getStrProperty(soapObject, "legalPerson");
        this.pack = StringUtils.getStrProperty(soapObject, "pack");
        this.productCer = StringUtils.getStrProperty(soapObject, "productCer");
        this.productCerNo = StringUtils.getStrProperty(soapObject, "productCerNo");
        this.sampleCode = StringUtils.getStrProperty(soapObject, "sampleCode");
        this.samplingCardinal = StringUtils.getStrProperty(soapObject, "samplingCardinal");
        this.samplingCount = StringUtils.getStrProperty(soapObject, "samplingCount");
        this.samplingNo = StringUtils.getStrProperty(soapObject, "samplingNo");
        this.specifications = StringUtils.getStrProperty(soapObject, "specifications");
        this.stall = StringUtils.getStrProperty(soapObject, "stall");
        this.telphone = StringUtils.getStrProperty(soapObject, "telphone");
        this.telphone2 = StringUtils.getStrProperty(soapObject, "telphone2");
        this.tradeMark = StringUtils.getStrProperty(soapObject, "tradeMark");
        this.unitAddress = StringUtils.getStrProperty(soapObject, "unitAddress");
        this.unitName = StringUtils.getStrProperty(soapObject, "unitName");
        this.unitProperties = StringUtils.getStrProperty(soapObject, "unitProperties");
        this.zipCode = StringUtils.getStrProperty(soapObject, "zipCode");
    }
}
