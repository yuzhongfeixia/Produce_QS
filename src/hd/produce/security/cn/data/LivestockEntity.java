/**
 * LivestockEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package hd.produce.security.cn.data;

import hd.produce.security.cn.annotation.Except;
import hd.utils.cn.StringUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * @Title: Entity
 * @Description: 畜禽抽样信息
 * @author nky
 * @date 2013-11-09 14:05:06
 * @version V1.0
 */
public class LivestockEntity implements java.io.Serializable {
    private static final long serialVersionUID = 5310673686290000881L;
    /** ID */
    private java.lang.String id;
    /** 样品编号 */
    private java.lang.String sampleCode;
    /** 注册商标 */
    private java.lang.String tradeMark;
    /** 包装 */
    private java.lang.String pack;
    /** 签封标志 */
    private java.lang.String signFlg;
    /** 畜主/货主 */
    private java.lang.String cargoOwner;
    /** 动物产地/来源 */
    private java.lang.String animalOrigin;
    /** 检疫证号 */
    private java.lang.String cardNumber;
    /** 抽样依据 */
    private java.lang.String taskSource;
    /** 抽样数量 */
    private java.lang.String samplingCount;
    /** 抽样基数 */
    private java.lang.String samplingBaseCount;
    /** 保存情况 */
    private java.lang.String saveSaveSituation;
    /** 抽样方式 */
    private java.lang.String samplingMode;
    /** 抽样单ID */
    private java.lang.String samplingMonadId;
    /** 样品名称(回显用) */
    @Except
    private java.lang.String sampleName;
    /** 样品图片(回显用) */
    @Except
    private java.lang.String samplePath;
    /** 条码 **/
    @Except
    private java.lang.String dCode;
    /** 任务编码 **/
    @Except
    private java.lang.String taskCode;
    /** 备注 */
    @Except
    private java.lang.String remark;
    /** 抽样详细地址 */
    @Except
    private java.lang.String samplingAddress;
    /** 图片内容 */
    @Except
    private String imgContent;
    /** 抽样单创建时间 */
    @Except
    private String samplingTime;

    public java.lang.String getdCode() {
        return dCode;
    }

    public void setdCode(java.lang.String dCode) {
        this.dCode = dCode;
    }

    public java.lang.String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(java.lang.String taskCode) {
        this.taskCode = taskCode;
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
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样详细地址
     */
    public java.lang.String getSamplingAddress() {
        return samplingAddress;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样详细地址
     */
    public void setSamplingAddress(java.lang.String samplingAddress) {
        this.samplingAddress = samplingAddress;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 备注
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

    /**
     * 方法: 设置String
     * 
     * @param: String 设置图片内容
     */
    public void setImgContent(String imgContent) {
        this.imgContent = imgContent;
    }

    /**
     * 方法: 取得String
     * 
     * @return: String 取得图片内容
     */
    public String getImgContent() {
        return imgContent;
    }

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
     * @return: java.lang.String 签封标志
     */
    public java.lang.String getSignFlg() {
        return this.signFlg;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 签封标志
     */
    public void setSignFlg(java.lang.String signFlg) {
        this.signFlg = signFlg;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 畜主/货主
     */
    public java.lang.String getCargoOwner() {
        return this.cargoOwner;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 畜主/货主
     */
    public void setCargoOwner(java.lang.String cargoOwner) {
        this.cargoOwner = cargoOwner;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 动物产地/来源
     */
    public java.lang.String getAnimalOrigin() {
        return this.animalOrigin;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 动物产地/来源
     */
    public void setAnimalOrigin(java.lang.String animalOrigin) {
        this.animalOrigin = animalOrigin;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 检疫证号
     */
    public java.lang.String getCardNumber() {
        return this.cardNumber;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 检疫证号
     */
    public void setCardNumber(java.lang.String cardNumber) {
        this.cardNumber = cardNumber;
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
     * @return: java.lang.String 保存情况
     */
    public java.lang.String getSaveSaveSituation() {
        return this.saveSaveSituation;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 保存情况
     */
    public void setSaveSaveSituation(java.lang.String saveSaveSituation) {
        this.saveSaveSituation = saveSaveSituation;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样方式
     */
    public java.lang.String getSamplingMode() {
        return this.samplingMode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样方式
     */
    public void setSamplingMode(java.lang.String samplingMode) {
        this.samplingMode = samplingMode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样单ID
     */
    public java.lang.String getSamplingMonadId() {
        return samplingMonadId;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样单ID
     */
    public void setSamplingMonadId(java.lang.String samplingMonadId) {
        this.samplingMonadId = samplingMonadId;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品名称
     */
    public java.lang.String getSampleName() {
        return sampleName;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品名称
     */
    public void setSampleName(java.lang.String sampleName) {
        this.sampleName = sampleName;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品图片
     */
    public java.lang.String getSamplePath() {
        return this.samplePath;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品图片
     */
    public void setSamplePath(java.lang.String samplePath) {
        this.samplePath = samplePath;
    }

    public String getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        try {
            this.id = StringUtils.getStrProperty(soapObject, "id");
            this.sampleCode = StringUtils.getStrProperty(soapObject, "sampleCode");
            this.tradeMark = StringUtils.getStrProperty(soapObject, "tradeMark");
            this.pack = StringUtils.getStrProperty(soapObject, "pack");
            this.signFlg = StringUtils.getStrProperty(soapObject, "signFlg");
            this.cargoOwner = StringUtils.getStrProperty(soapObject, "cargoOwner");
            this.animalOrigin = StringUtils.getStrProperty(soapObject, "animalOrigin");
            this.cardNumber = StringUtils.getStrProperty(soapObject, "cardNumber");
            this.taskSource = StringUtils.getStrProperty(soapObject, "taskSource");
            this.samplingCount = StringUtils.getStrProperty(soapObject, "samplingCount");
            this.samplingBaseCount = StringUtils.getStrProperty(soapObject, "samplingBaseCount");
            this.saveSaveSituation = StringUtils.getStrProperty(soapObject, "saveSaveSituation");
            this.samplingMode = StringUtils.getStrProperty(soapObject, "samplingMode");
            this.samplingMonadId = StringUtils.getStrProperty(soapObject, "samplingMonadId");
            this.sampleName = StringUtils.getStrProperty(soapObject, "sampleName");
            this.samplePath = StringUtils.getStrProperty(soapObject, "samplePath");
            this.dCode = StringUtils.getStrProperty(soapObject, "dCode");
            this.taskCode = StringUtils.getStrProperty(soapObject, "taskCode");
            this.remark = StringUtils.getStrProperty(soapObject, "remark");
            this.samplingAddress = StringUtils.getStrProperty(soapObject, "samplingAddress");
            this.imgContent = StringUtils.getStrProperty(soapObject, "imgContent");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
