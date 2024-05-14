package hd.produce.security.cn.data;

import hd.produce.security.cn.annotation.Except;
import hd.utils.cn.Constants;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

/**
 * @author dxz
 * @version V1.0
 * @Title: Entity
 * @Description: 抽样信息
 * @date 2013-11-07 12:55:29
 */
public class SampleInfoEntity implements Serializable, Cloneable {
    /**
     * 
     */
    private static final long serialVersionUID = 2275929578944365052L;

    /**
     * ID
     */
    private String id;

    /**
     * 监测任务
     */
    private String taskCode;

    /**
     * 二维码
     */
    private String dCode;

    /**
     * 样品图片
     */
    private String samplePath;

    /**
     * 样品图片地址
     */
    private String serverUrlPathImg;

    /**
     * 经度地理坐标
     */
    private String longitude;

    /**
     * 纬度地理坐标
     */
    private String latitude;

    /**
     * 农产品code
     */
    private String agrCode;

    /**
     * 抽样地市
     */
    private String cityCode;

    /**
     * 抽样环节
     */
    private String monitoringLink;

    /**
     * 抽样环节名称(Sprinner显示用)
     */
    private String monitoringLinkName;

    /**
     * 抽样人(Pad)
     */
    private String padId;

    /**
     * 抽样时间
     */
    private String samplingDate;

    /**
     * 抽样时间(导出用)
     */
    private String samplingDateStr;

    /**
     * 单位code(受检)
     */
    private String unitFullcode;
    /**
     * 单位全称(受检)
     */
    private String unitFullname;

    /**
     * 通讯地址(受检)
     */
    private String unitAddress;

    /**
     * 邮编(受检)
     */
    private String zipCode;

    /**
     * 法定代表人(受检)
     */
    private String legalPerson;

    /**
     * 联系人(受检)
     */
    private String contact;

    /**
     * 电话(受检)
     */
    private String telphone;

    /**
     * 传真(受检)
     */
    private String fax;

    /**
     * 备注
     */
    private String remark;

    /**
     * 样品编号
     */
    private String sampleCode;

    /**
     * 实验室编码
     */
    private String labCode;

    /**
     * 样品是否合格
     */
    private String isQualified;

    /**
     * 抽样单状态
     */
    private String sampleStatus;

    /**
     * 检测机构
     */
    private String detectionCode;

    /**
     * 制样编码
     */
    private String spCode;

    /**
     * 抽样区县
     */
    private String countyCode;

    /**
     * 检测项目名称
     */
    private String projectName;

    /**
     * 样品名称(搜索条件)
     */
    private String sampleName;

    /**
     * 项目编码(搜索条件)
     */
    @Except
    private String projectCode;

    /**
     * 信息完整度
     */
    private String complete;

    /**
     * 上报时间
     */
    private String reportingDate;

    /**
     * 抽样单位
     */
    private String samplingOrgCode;

    /**
     * 监测类型
     */
    @Except
    private String monitorType;

    /**
     * 年度
     */
    @Except
    private String year;

    /**
     * 质检机构code(搜索条件)
     */
    @Except
    private String orgCode;

    /**
     * 打印数量
     */
    private Integer printCount;

    /**
     * 地区(导出用)
     */
    private String cityAndCountry;

    /**
     * 抽样详细地址
     */
    private String samplingAddress;

    /**
     * 抽样人员姓名
     */
    private String samplingPersons;

    /**
     * 图片-主表ID
     */
    @Except
    private String imgContent;

    /** 抽样单ID */
    private String samplingMonadId;

    /**
     * 抽样单创建时间
     */
    private String samplingTime;
    /**
     * 抽样单模板(备份本地抽样单用)
     */
    @Except
    private String templete;

    public SampleInfoEntity() {
    }

    /**
     * 例行监测类信息--
     */
    @Except
    private List<RoutinemonitoringEntity> routinemonitoringList;

    /**
     * 普查类信息 *
     */
    @Except
    private GeneralcheckEntity generalcheckEntity;

    /**
     * 监督抽查类信息
     */
    @Except
    private SuperviseCheckEntity superviseCheckEntity;

    /**
     * 生鲜乳实体
     */
    @Except
    private FreshMilkEntity nkyFreshMilkEntity;

    /**
     * 畜禽类信息
     */
    @Except
    private List<LivestockEntity> livestockEntityList;

    public List<RoutinemonitoringEntity> getRoutinemonitoringList() {
        return routinemonitoringList;
    }

    public void setRoutinemonitoringList(List<RoutinemonitoringEntity> routinemonitoringList) {
        this.routinemonitoringList = routinemonitoringList;
    }

    public GeneralcheckEntity getGeneralcheckEntity() {
        return generalcheckEntity;
    }

    public void setGeneralcheckEntity(GeneralcheckEntity generalcheckEntity) {
        this.generalcheckEntity = generalcheckEntity;
    }

    public SuperviseCheckEntity getSuperviseCheckEntity() {
        return superviseCheckEntity;
    }

    public void setSuperviseCheckEntity(SuperviseCheckEntity superviseCheckEntity) {
        this.superviseCheckEntity = superviseCheckEntity;
    }

    public FreshMilkEntity getFreshMilkEntity() {
        return nkyFreshMilkEntity;
    }

    public void setFreshMilkEntity(FreshMilkEntity freshMilkEntity) {
        this.nkyFreshMilkEntity = freshMilkEntity;
    }

    public List<LivestockEntity> getLivestockEntityList() {
        return livestockEntityList;
    }

    public void setLivestockEntityList(List<LivestockEntity> livestockEntity) {
        this.livestockEntityList = livestockEntity;
    }

    public String getSamplingAddress() {
        return samplingAddress;
    }

    public void setSamplingAddress(String samplingAddress) {
        this.samplingAddress = samplingAddress;
    }

    public String getSamplingPersons() {
        return samplingPersons;
    }

    public void setSamplingPersons(String samplingPersons) {
        this.samplingPersons = samplingPersons;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String ID
     */

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

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 监测任务
     */
    public String getTaskCode() {
        return taskCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 监测任务
     */
    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 二维码
     */
    public String getdCode() {
        return dCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 二维码
     */
    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品图片
     */
    public String getSamplePath() {
        return this.samplePath;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品图片
     */
    public void setSamplePath(String samplePath) {
        this.samplePath = samplePath;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 农产品code
     */
    public String getAgrCode() {
        return this.agrCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 农产品code
     */
    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样地市
     */
    public String getCityCode() {
        return this.cityCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样地市
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样环节
     */
    public String getMonitoringLink() {
        return this.monitoringLink;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样环节
     */
    public void setMonitoringLink(String monitoringLink) {
        this.monitoringLink = monitoringLink;
    }

    public String getMonitoringLinkName() {
        return monitoringLinkName;
    }

    public void setMonitoringLinkName(String monitoringLinkName) {
        this.monitoringLinkName = monitoringLinkName;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样人(Pad)
     */
    public String getPadId() {
        return this.padId;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样人(Pad)
     */
    public void setPadId(String padId) {
        this.padId = padId;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 抽样时间
     */
    public String getSamplingDate() {
        return this.samplingDate;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 抽样时间
     */
    public void setSamplingDate(String samplingDate) {
        this.samplingDate = samplingDate;
    }

    public String getUnitFullcode() {
        return unitFullcode;
    }

    public void setUnitFullcode(String unitFullcode) {
        this.unitFullcode = unitFullcode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位全称(受检)
     */
    public String getUnitFullname() {
        return this.unitFullname;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 单位全称(受检)
     */
    public void setUnitFullname(String unitFullname) {
        this.unitFullname = unitFullname;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 通讯地址(受检)
     */
    public String getUnitAddress() {
        return this.unitAddress;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 通讯地址(受检)
     */
    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 邮编(受检)
     */
    public String getZipCode() {
        return this.zipCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 邮编(受检)
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 法定代表人(受检)
     */
    public String getLegalPerson() {
        return this.legalPerson;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 法定代表人(受检)
     */
    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 联系人(受检)
     */
    public String getContact() {
        return this.contact;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 联系人(受检)
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 电话(受检)
     */
    public String getTelphone() {
        return this.telphone;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 电话(受检)
     */
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 传真(受检)
     */
    public String getFax() {
        return this.fax;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 传真(受检)
     */
    public void setFax(String fax) {
        this.fax = fax;
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

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品编号
     */
    public String getSampleCode() {
        return this.sampleCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品编号
     */
    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 实验室编码
     */
    public String getLabCode() {
        return this.labCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 实验室编码
     */
    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品是否合格
     */
    public String getIsQualified() {
        return this.isQualified;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品是否合格
     */
    public void setIsQualified(String isQualified) {
        this.isQualified = isQualified;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样单状态
     */
    public String getSampleStatus() {
        return this.sampleStatus;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样单状态
     */
    public void setSampleStatus(String sampleStatus) {
        this.sampleStatus = sampleStatus;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 检测机构
     */
    public String getDetectionCode() {
        return this.detectionCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 检测机构
     */
    public void setDetectionCode(String detectionCode) {
        this.detectionCode = detectionCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 制样编码
     */
    public String getSpCode() {
        return this.spCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 制样编码
     */
    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样区县
     */
    public String getCountyCode() {
        return this.countyCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样区县
     */
    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 样品名称
     */
    public String getSampleName() {
        return sampleName;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 样品名称
     */
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 项目编码
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 项目编码
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 信息完整度
     */
    public String getComplete() {
        return this.complete;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 上报时间
     */
    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 上报时间
     */
    public String getReportingDate() {
        return this.reportingDate;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 抽样单位
     */
    public void setSamplingOrgCode(String samplingOrgCode) {
        this.samplingOrgCode = samplingOrgCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抽样单位
     */
    public String getSamplingOrgCode() {
        return this.samplingOrgCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 信息完整度
     */
    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 取得地区
     */
    public String getCityAndCountry() {
        return cityAndCountry;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 设置地区
     */
    public void setCityAndCountry(String cityAndCountry) {
        this.cityAndCountry = cityAndCountry;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 取得抽样时间
     */
    public String getSamplingDateStr() {
        return samplingDateStr;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 设置抽样时间
     */
    public void setSamplingDateStr(String samplingDateStr) {
        this.samplingDateStr = samplingDateStr;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    /**
     * 方法: 设置String
     * 
     * @param: String 图片-主表ID
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

    public String getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }

    public String getTemplete() {
        return templete;
    }

    public void setTemplete(String templete) {
        this.templete = templete;
    }

    public String getSamplingMonadId() {
        return samplingMonadId;
    }

    public void setSamplingMonadId(String samplingMonadId) {
        this.samplingMonadId = samplingMonadId;
    }

    public SampleInfoEntity clone() {
        SampleInfoEntity o = null;
        try {
            o = (SampleInfoEntity) super.clone();
            // 克隆时不要子表
            o.setRoutinemonitoringList(null);
            o.setGeneralcheckEntity(null);
            o.setSuperviseCheckEntity(null);
            o.setFreshMilkEntity(null);
            o.setLivestockEntityList(null);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public void parseBySoapObject(SoapObject soapObject) {
        try {
            this.id = StringUtils.getStrProperty(soapObject, "id");
            this.taskCode = StringUtils.getStrProperty(soapObject, "taskCode");
            this.dCode = StringUtils.getStrProperty(soapObject, "dCode");
            this.samplePath = StringUtils.getStrProperty(soapObject, "samplePath");
            this.longitude = StringUtils.getStrProperty(soapObject, "longitude");
            this.latitude = StringUtils.getStrProperty(soapObject, "latitude");
            this.agrCode = StringUtils.getStrProperty(soapObject, "agrCode");
            this.cityCode = StringUtils.getStrProperty(soapObject, "cityCode");
            this.monitoringLink = StringUtils.getStrProperty(soapObject, "monitoringLink");
            this.padId = StringUtils.getStrProperty(soapObject, "padId");
            this.samplingDate = StringUtils.getStrProperty(soapObject, "samplingDate");
            this.samplingDateStr = StringUtils.getStrProperty(soapObject, "samplingDateStr");
            this.unitFullname = StringUtils.getStrProperty(soapObject, "unitFullname");
            this.unitAddress = StringUtils.getStrProperty(soapObject, "unitAddress");
            this.zipCode = StringUtils.getStrProperty(soapObject, "zipCode");
            this.legalPerson = StringUtils.getStrProperty(soapObject, "legalPerson");
            this.contact = StringUtils.getStrProperty(soapObject, "contact");
            this.telphone = StringUtils.getStrProperty(soapObject, "telphone");
            this.fax = StringUtils.getStrProperty(soapObject, "fax");
            this.remark = StringUtils.getStrProperty(soapObject, "remark");
            this.sampleCode = StringUtils.getStrProperty(soapObject, "sampleCode");
            this.labCode = StringUtils.getStrProperty(soapObject, "labCode");
            this.isQualified = StringUtils.getStrProperty(soapObject, "isQualified");
            this.sampleStatus = StringUtils.getStrProperty(soapObject, "sampleStatus");
            this.detectionCode = StringUtils.getStrProperty(soapObject, "detectionCode");
            this.spCode = StringUtils.getStrProperty(soapObject, "spCode");
            this.countyCode = StringUtils.getStrProperty(soapObject, "countyCode");
            this.projectName = StringUtils.getStrProperty(soapObject, "projectName");
            this.sampleName = StringUtils.getStrProperty(soapObject, "sampleName");
            this.projectCode = StringUtils.getStrProperty(soapObject, "projectCode");
            this.complete = StringUtils.getStrProperty(soapObject, "complete");
            this.reportingDate = StringUtils.getStrProperty(soapObject, "reportingDate");
            this.samplingOrgCode = StringUtils.getStrProperty(soapObject, "samplingOrgCode");
            this.monitorType = StringUtils.getStrProperty(soapObject, "monitorType");
            this.year = StringUtils.getStrProperty(soapObject, "year");
            this.orgCode = StringUtils.getStrProperty(soapObject, "orgCode");
            this.printCount = StringUtils.getIntProperty(soapObject, "printCount");
            this.cityAndCountry = StringUtils.getStrProperty(soapObject, "cityAndCountry");
            this.serverUrlPathImg = StringUtils.getStrProperty(soapObject, "serverUrlPathImg");
            this.imgContent = StringUtils.getStrProperty(soapObject, "imgContent");
            this.samplingAddress = StringUtils.getStrProperty(soapObject, "samplingAddress");
            this.samplingPersons = StringUtils.getStrProperty(soapObject, "samplingPersons");
            // 抽样单创建时间
            this.samplingTime = ConverterUtil.toString(soapObject.getProperty("samplingTime"));
            if (soapObject.hasProperty("routinemonitoringList")) {
                routinemonitoringList = new ArrayList<RoutinemonitoringEntity>();
                SoapObject roSo = (SoapObject) soapObject.getProperty("routinemonitoringList");
                RoutinemonitoringEntity ro = new RoutinemonitoringEntity();
                ro.parseBySoapObject(roSo);

                routinemonitoringList.add(ro);
            }
            // int size = roSo.getPropertyCount();
            // LogUtil.i(size+"");
            //
            // if (size == 1) {
            // SoapObject child = (SoapObject)
            // soapObject.getProperty("routinemonitoringList");
            // RoutinemonitoringEntity ro = new RoutinemonitoringEntity();
            // ro.parseBySoapObject(child);
            // routinemonitoringList.add(ro);
            // } else {
            // for (int i = 0; i < size; i++) {
            // SoapObject child = (SoapObject) roSo.getProperty(i);
            // RoutinemonitoringEntity ro = new RoutinemonitoringEntity();
            // ro.parseBySoapObject(child);
            // routinemonitoringList.add(ro);
            // }
            // }

            if (soapObject.hasProperty("generalcheckEntity")) {
                SoapObject geSo = (SoapObject) soapObject.getProperty("generalcheckEntity");
                this.generalcheckEntity = new GeneralcheckEntity();
                generalcheckEntity.parseBySoapObject(geSo);
            }
            if (soapObject.hasProperty("superviseCheckEntity")) {
                SoapObject suSo = (SoapObject) soapObject.getProperty("superviseCheckEntity");
                this.superviseCheckEntity = new SuperviseCheckEntity();
                superviseCheckEntity.parseBySoapObject(suSo);
            }
            if (soapObject.hasProperty("nkyFreshMilkEntity")) {
                SoapObject frSo = (SoapObject) soapObject.getProperty("nkyFreshMilkEntity");
                this.nkyFreshMilkEntity = new FreshMilkEntity();
                nkyFreshMilkEntity.parseBySoapObject(frSo);
            }
            if (soapObject.hasProperty("livestockEntityList")) {
                livestockEntityList = new ArrayList<LivestockEntity>();
                SoapObject liSo = (SoapObject) soapObject.getProperty("livestockEntityList");
                LivestockEntity livestockEntity = new LivestockEntity();
                livestockEntity.parseBySoapObject(liSo);
                livestockEntityList.add(livestockEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getServerUrlPathImg() {
        return serverUrlPathImg;
    }

    public void setServerUrlPathImg(String serverUrlPathImg) {
        this.serverUrlPathImg = serverUrlPathImg;
    }

    public void putDataToSubTable(String type) {
        // 例行监测抽样单
        if (Constants.TEMPLETE_ROUTINE_MONITORING.equals(type)) {
            if (ConverterUtil.isNotEmpty(getRoutinemonitoringList())) {
                for (RoutinemonitoringEntity rmEntity : getRoutinemonitoringList()) {
                    if (rmEntity.getSampleCode().equals(getSampleCode())) {
                        // 条码
                        rmEntity.setdCode(getdCode());
                        // 图片路径
                        rmEntity.setSamplePath(getSamplePath());
                        // 农产品code
                        rmEntity.setAgrCode(getAgrCode());
                        // 农场品名称
                        rmEntity.setSampleName(getSampleName());
                        // 备注
                        rmEntity.setRemark(getRemark());
                        // 抽样详细地址
                        rmEntity.setSamplingAddress(getSamplingAddress());
                        // 抽样单创建时间
                        rmEntity.setSamplingTime(getSamplingTime());
                        break;
                    }
                }
            }
        }
        // 畜禽抽样单
        if (Constants.TEMPLETE_LIVESTOCK.equals(type)) {
            if (ConverterUtil.isNotEmpty(getLivestockEntityList())) {
                for (LivestockEntity ltEntity : getLivestockEntityList()) {
                    if (ltEntity.getSampleCode().equals(getSampleCode())) {
                        ltEntity.setSamplePath(getSamplePath());
                    }
                }
            }
        }
    }
}
