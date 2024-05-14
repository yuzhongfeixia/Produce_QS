package hd.produce.security.cn.entity;

/**
 * 例行监测抽样单表
 * 
 * <p>
 * 子表中另有其他属性，pad端用不到的就没有定义
 * 
 * @author xudl(Wisea)
 * 
 */
public class TbRoutineMonitoring extends TbSampleInfo {
    // 样品来源
    private String sampleSource;
    // 样品量(n/N)
    private String sampleCount;
    // 任务来源
    private String taskSource;
    // 执行标准
    private String execStanderd;

    public String getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(String sampleSource) {
        this.sampleSource = sampleSource;
    }

    public String getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(String sampleCount) {
        this.sampleCount = sampleCount;
    }

    public String getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(String taskSource) {
        this.taskSource = taskSource;
    }

    public String getExecStanderd() {
        return execStanderd;
    }

    public void setExecStanderd(String execStanderd) {
        this.execStanderd = execStanderd;
    }

}
