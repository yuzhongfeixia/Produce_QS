package hd.produce.security.cn.data;

public class PadTCInfo {
    //当前替抽PadCode
    private String thisPadCode;
    //当前替抽Pad用户名
    private String thisPadName;
    //替抽数量
    private String count;
    //被替抽PadCode
    private String thePadCode;
    //被替抽Pad用户名
    private String thePadName;
    //当前抽样的任务Code
    private String TaskCode;
    //当前转发的任务名称
    private String taskName;

    public String getThisPadCode() {
        return thisPadCode;
    }

    public void setThisPadCode(String thisPadCode) {
        this.thisPadCode = thisPadCode;
    }

    public String getThisPadName() {
        return thisPadName;
    }

    public void setThisPadName(String thisPadName) {
        this.thisPadName = thisPadName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getThePadCode() {
        return thePadCode;
    }

    public void setThePadCode(String thePadCode) {
        this.thePadCode = thePadCode;
    }

    public String getThePadName() {
        return thePadName;
    }

    public void setThePadName(String thePadName) {
        this.thePadName = thePadName;
    }

    public String getTaskCode() {
        return TaskCode;
    }

    public void setTaskCode(String taskCode) {
        TaskCode = taskCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
