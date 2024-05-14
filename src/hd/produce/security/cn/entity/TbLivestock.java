package hd.produce.security.cn.entity;

/**
 * 畜禽抽样单表
 * <p>
 * 子表中另有其他属性，pad端用不到的就没有定义
 * 
 * @author xudl(Wisea)
 * 
 */
public class TbLivestock extends TbSampleInfo {
    // 商标
    private String tradeMark;
    // 包装
    private String pack;
    // 规格
    private String specifications;
    // 标识
    private String flag;
    // 签封标志
    private String signFlg;
    // 畜主/货主
    private String cargoOwner;
    // 动物产地/来源
    private String animalOrigin;
    // 检疫证号
    private String cardNumber;
    // 抽样依据
    private String taskSource;
    // 抽样数量
    private String samplingCount;
    // 抽样基数
    private String samplingBaseCount;
    // 保存情况
    private String saveSaveSituation;
    // 抽样方式
    private String samplingMode;

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

    public String getSignFlg() {
        return signFlg;
    }

    public void setSignFlg(String signFlg) {
        this.signFlg = signFlg;
    }

    public String getCargoOwner() {
        return cargoOwner;
    }

    public void setCargoOwner(String cargoOwner) {
        this.cargoOwner = cargoOwner;
    }

    public String getAnimalOrigin() {
        return animalOrigin;
    }

    public void setAnimalOrigin(String animalOrigin) {
        this.animalOrigin = animalOrigin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(String taskSource) {
        this.taskSource = taskSource;
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

    public String getSaveSaveSituation() {
        return saveSaveSituation;
    }

    public void setSaveSaveSituation(String saveSaveSituation) {
        this.saveSaveSituation = saveSaveSituation;
    }

    public String getSamplingMode() {
        return samplingMode;
    }

    public void setSamplingMode(String samplingMode) {
        this.samplingMode = samplingMode;
    }

}
