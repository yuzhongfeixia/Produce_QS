package hd.produce.security.cn.entity;

/**
 * 监督抽查抽样单表
 * <p>
 * 子表中另有其他属性，pad端用不到的就没有定义
 * 
 * @author xudl(Wisea)
 * 
 */
public class TbSuperviseCheck extends TbSampleInfo {
    // 商标
    private String tradeMark;
    // 包装
    private String pack;
    // 规格
    private String specifications;
    // 标识
    private String flag;
    // 生产日期或批号
    private String batchNumber;
    // 执行标准
    private String execStanderd;
    // 产品认证情况
    private String productCer;
    // 证书编号
    private String productCerNo;
    // 获证日期
    private String certificateTime;
    // 抽样数量
    private String samplingCount;
    // 抽样基数
    private String samplingBaseCount;
    // 通知书编号及有效期
    private String noticeDetails;

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

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getExecStanderd() {
        return execStanderd;
    }

    public void setExecStanderd(String execStanderd) {
        this.execStanderd = execStanderd;
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

    public String getCertificateTime() {
        return certificateTime;
    }

    public void setCertificateTime(String certificateTime) {
        this.certificateTime = certificateTime;
    }

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

    public String getNoticeDetails() {
        return noticeDetails;
    }

    public void setNoticeDetails(String noticeDetails) {
        this.noticeDetails = noticeDetails;
    }

}
