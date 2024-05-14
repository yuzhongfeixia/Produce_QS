package hd.produce.security.cn.entity;

/**
 * 抽样单主表
 * 
 * @author xudl(Wisea)
 *
 */
public class TbSampleInfo {
    // ID
    private String id;
    // 监测任务ID
    private String taskCode;
    // 二维码
    private String dCode;
    // 样品图片
    private String samplePath;
    // 经度地理坐标
    private String longitude;
    // 纬度地理坐标
    private String latitude;
    // 农产品CODE
    private String agrCode;
    // 抽样地市
    private String cityCode;
    // 抽样区县
    private String countyCode;
    // 地区名称(接口返回)
    private String cityAndCountry;
    // 抽样环节
    private String monitoringLink;
    // 抽样环境名称(接口返回)
    private String monitoringLinkName;
    // 抽样人(Pad)
    private String padId;
    // 抽样时间
    private String samplingDate;
    // 单位编码(受检)
    private String unitFullcode;
    // 单位全称(受检)
    private String unitFullname;
    // 通讯地址(受检)
    private String unitAddress;
    // 邮编(受检)
    private String zipCode;
    // 法定代表人(受检)
    private String legalPerson;
    // 联系人(受检)
    private String contact;
    // 电话(受检)
    private String telphone;
    // 传真(受检)
    private String fax;
    // 备注
    private String remark;
    // 样品编号(与子表关联的ID)
    private String sampleCode;
    // 样品名称(接口返回)
    private String sampleName;
    // 实验室编码(pad端无用)
    // private String labCode;
    // 样品是否合格(pad端无用)
    // private String isQualified;
    // 抽样单状态
    private String sampleStatus;
    // 检测机构(pad端无用)
    // private String detectionCode;
    // 制样编码
    private String spCode;
    // 信息完整度(pad端无用)
    // private String complete;
    // 上报时间(pad端无用)
    // private String reportingDate;
    // 抽样单位
    private String samplingOrgCode;
    // 打印数量(pad端无用)
    // private Integer printCount;
    // 检测上报时间(pad端无用)
    // private String detectionReportingDate;
    // 抽样详细地址
    private String samplingAddress;
    // 抽样人员姓名
    private String samplingPersons;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public String getSamplePath() {
        return samplePath;
    }

    public void setSamplePath(String samplePath) {
        this.samplePath = samplePath;
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

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCityAndCountry() {
        return cityAndCountry;
    }

    public void setCityAndCountry(String cityAndCountry) {
        this.cityAndCountry = cityAndCountry;
    }

    public String getMonitoringLink() {
        return monitoringLink;
    }

    public void setMonitoringLink(String monitoringLink) {
        this.monitoringLink = monitoringLink;
    }

    public String getMonitoringLinkName() {
        return monitoringLinkName;
    }

    public void setMonitoringLinkName(String monitoringLinkName) {
        this.monitoringLinkName = monitoringLinkName;
    }

    public String getPadId() {
        return padId;
    }

    public void setPadId(String padId) {
        this.padId = padId;
    }

    public String getSamplingDate() {
        return samplingDate;
    }

    public void setSamplingDate(String samplingDate) {
        this.samplingDate = samplingDate;
    }

    public String getUnitFullcode() {
        return unitFullcode;
    }

    public void setUnitFullcode(String unitFullcode) {
        this.unitFullcode = unitFullcode;
    }

    public String getUnitFullname() {
        return unitFullname;
    }

    public void setUnitFullname(String unitFullname) {
        this.unitFullname = unitFullname;
    }

    public String getUnitAddress() {
        return unitAddress;
    }

    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSampleStatus() {
        return sampleStatus;
    }

    public void setSampleStatus(String sampleStatus) {
        this.sampleStatus = sampleStatus;
    }

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    public String getSamplingOrgCode() {
        return samplingOrgCode;
    }

    public void setSamplingOrgCode(String samplingOrgCode) {
        this.samplingOrgCode = samplingOrgCode;
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

    public String getSamplingMonadId() {
        return samplingMonadId;
    }

    public void setSamplingMonadId(String samplingMonadId) {
        this.samplingMonadId = samplingMonadId;
    }

    // 抽样单ID
    private String samplingMonadId;

}
