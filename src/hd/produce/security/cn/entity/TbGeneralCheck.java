package hd.produce.security.cn.entity;

/**
 * 普查(风险)抽样单表
 * <p>
 * 子表中另有其他属性，pad端用不到的就没有定义
 * 
 * @author xudl(Wisea)
 * 
 */
public class TbGeneralCheck extends TbSampleInfo {
    // 商标
    private String tradeMark;
    // 包装
    private String pack;
    // 规格
    private String specifications;
    // 标识
    private String flag;
    // 执行标准
    private String execStanderd;
    // 生产日期或批号
    private String batchNumber;
    // 产品认证情况
    private String productCer;
    // 证书编号
    private String productCerNo;
    // 抽样数量
    private String samplingCount;
    // 受检人与摊位号
    private String stall;
    // 电话
    private String telphone;
    // 传真
    private String fax;
    // 单位性质(生产)
    private String unitProperties;
    // 单位名称(生产)
    private String unitName;
    // 通讯地址(生产)
    private String unitAddress;
    // 邮编(生产)
    private String zipCode;
    // 法定代表人(生产)
    private String legalPerson;
    // 联系人(生产)
    private String contacts;
    // 电话(生产)
    private String telphone2;
    // 传真(生产)
    private String fax2;
    // 抽样文件编号
    private String samplingNo;
    // 等级
    private String grade;
    // 抽样基数
    private String samplingCardinal;

    public String getTradeMark() {
        return tradeMark;
    }

    public void setTradeMark(String tradeMark) {
        this.tradeMark = tradeMark;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getExecStanderd() {
        return execStanderd;
    }

    public void setExecStanderd(String execStanderd) {
        this.execStanderd = execStanderd;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getProductCer() {
        return productCer;
    }

    public void setProductCer(String productCer) {
        this.productCer = productCer;
    }

    public String getProductCerNo() {
        return productCerNo;
    }

    public void setProductCerNo(String productCerNo) {
        this.productCerNo = productCerNo;
    }

    public String getSamplingCount() {
        return samplingCount;
    }

    public void setSamplingCount(String samplingCount) {
        this.samplingCount = samplingCount;
    }

    public String getStall() {
        return stall;
    }

    public void setStall(String stall) {
        this.stall = stall;
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

    public String getUnitProperties() {
        return unitProperties;
    }

    public void setUnitProperties(String unitProperties) {
        this.unitProperties = unitProperties;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getTelphone2() {
        return telphone2;
    }

    public void setTelphone2(String telphone2) {
        this.telphone2 = telphone2;
    }

    public String getFax2() {
        return fax2;
    }

    public void setFax2(String fax2) {
        this.fax2 = fax2;
    }

    public String getSamplingNo() {
        return samplingNo;
    }

    public void setSamplingNo(String samplingNo) {
        this.samplingNo = samplingNo;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSamplingCardinal() {
        return samplingCardinal;
    }

    public void setSamplingCardinal(String samplingCardinal) {
        this.samplingCardinal = samplingCardinal;
    }

}
