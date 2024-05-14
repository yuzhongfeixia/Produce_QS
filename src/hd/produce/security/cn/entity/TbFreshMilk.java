package hd.produce.security.cn.entity;

/**
 * 生鲜乳抽样单表
 * <p>
 * 子表中另有其他属性，pad端用不到的就没有定义
 * 
 * @author xudl(Wisea)
 * 
 */
public class TbFreshMilk extends TbSampleInfo {
    // 抽样数量
    private String samplingCount;
    // 抽样基数
    private String samplingBaseCount;
    // 生鲜乳类型
    private String type;
    // 类型备注
    private String typeRemark;
    // 生鲜乳收购许可证
    private String buyLicence;
    // 许可证号
    private String licenceNo;
    // 许可证备注
    private String licenceRemark;
    // 生鲜乳准运证
    private String navicert;
    // 准运证号
    private String navicertNo;
    // 准运证备注
    private String navicertRemark;
    // 生鲜乳交接单
    private String deliveryReceitp;
    // 交接单备注
    private String deliveryReceitpRemark;
    // 交奶去向
    private String direction;
    // 联系电话(法定代表人)
    private String telphone;
    // 受检人
    private String examinee;
    // 联系电话(受检人)
    private String telphone2;
    // 抽样日期和时间
    private String samplingDate;

    public String getSamplingCount() {
        return samplingCount;
    }

    public void setSamplingCount(String samplingCount) {
        this.samplingCount = samplingCount;
    }

    public String getSamplingBaseCount() {
        return samplingBaseCount;
    }

    public void setSamplingBaseCount(String samplingBaseCount) {
        this.samplingBaseCount = samplingBaseCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeRemark() {
        return typeRemark;
    }

    public void setTypeRemark(String typeRemark) {
        this.typeRemark = typeRemark;
    }

    public String getBuyLicence() {
        return buyLicence;
    }

    public void setBuyLicence(String buyLicence) {
        this.buyLicence = buyLicence;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public String getLicenceRemark() {
        return licenceRemark;
    }

    public void setLicenceRemark(String licenceRemark) {
        this.licenceRemark = licenceRemark;
    }

    public String getNavicert() {
        return navicert;
    }

    public void setNavicert(String navicert) {
        this.navicert = navicert;
    }

    public String getNavicertNo() {
        return navicertNo;
    }

    public void setNavicertNo(String navicertNo) {
        this.navicertNo = navicertNo;
    }

    public String getNavicertRemark() {
        return navicertRemark;
    }

    public void setNavicertRemark(String navicertRemark) {
        this.navicertRemark = navicertRemark;
    }

    public String getDeliveryReceitp() {
        return deliveryReceitp;
    }

    public void setDeliveryReceitp(String deliveryReceitp) {
        this.deliveryReceitp = deliveryReceitp;
    }

    public String getDeliveryReceitpRemark() {
        return deliveryReceitpRemark;
    }

    public void setDeliveryReceitpRemark(String deliveryReceitpRemark) {
        this.deliveryReceitpRemark = deliveryReceitpRemark;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getExaminee() {
        return examinee;
    }

    public void setExaminee(String examinee) {
        this.examinee = examinee;
    }

    public String getTelphone2() {
        return telphone2;
    }

    public void setTelphone2(String telphone2) {
        this.telphone2 = telphone2;
    }

    public String getSamplingDate() {
        return samplingDate;
    }

    public void setSamplingDate(String samplingDate) {
        this.samplingDate = samplingDate;
    }

}
