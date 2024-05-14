package hd.produce.security.cn.data;

/**
 * Created by Administrator on 13-11-25.
 * a item of a SampleList used in SampleListActivity.
 */
public class SampleInfo {

    private String sampleImage;

    private String sampleNum;

    private String prodeuceName;

    private String inspectedUnits;

    private String checkTime;

    private SampleInfoEntity sampleInfoEntity;

    public SampleInfo(String sampleImage, String sampleNum, String prodeuceName, String inspectedUnits, String checkTime) {
        this.sampleImage = sampleImage;
        this.sampleNum = sampleNum;
        this.prodeuceName = prodeuceName;
        this.inspectedUnits = inspectedUnits;
        this.checkTime = checkTime;
    }

    public SampleInfo(SampleInfoEntity sampleInfoEntity) {
        this.sampleImage = sampleInfoEntity.getSamplePath();
        this.sampleNum = sampleInfoEntity.getSampleName();
        this.prodeuceName = sampleInfoEntity.getProjectName();
        this.inspectedUnits = sampleInfoEntity.getUnitFullname();
        this.checkTime = sampleInfoEntity.getSamplingDate();
        this.sampleInfoEntity = sampleInfoEntity;
    }

    public String getSampleImage() {
        return sampleImage;
    }

    public void setSampleImage(String sampleImage) {
        this.sampleImage = sampleImage;
    }

    public String getSampleNum() {
        return sampleNum;
    }

    public String getProdeuceName() {
        return prodeuceName;
    }

    public void setProdeuceName(String prodeuceName) {
        this.prodeuceName = prodeuceName;
    }

    public String getInspectedUnits() {
        return inspectedUnits;
    }

    public void setInspectedUnits(String inspectedUnits) {
        this.inspectedUnits = inspectedUnits;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public SampleInfoEntity getSampleInfoEntity() {
        return sampleInfoEntity;
    }

    public void setSampleInfoEntity(SampleInfoEntity sampleInfoEntity) {
        this.sampleInfoEntity = sampleInfoEntity;
    }
}
