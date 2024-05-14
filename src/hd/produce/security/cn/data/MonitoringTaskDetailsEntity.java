package hd.produce.security.cn.data;

import hd.utils.cn.ConverterUtil;
import hd.utils.cn.StringUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * 任务详情
 * 
 * @author xudl(Wisea)
 */
public class MonitoringTaskDetailsEntity implements java.io.Serializable {
    /**
     * 序列ID
     */
    private static final long serialVersionUID = 8440451253591447109L;
    /**
     * ID
     */
    private String id;
    /**
     * 客户端(PAD)
     */
    private String padId;
    /**
     * 分配数量
     */
    private Integer taskCount;
    /**
     * 分配时间
     */
    private String assignTime;
    /**
     * 任务ID
     */
    private String taskCode;
    /**
     * 完成情况
     */
    private String taskStatus;
    /**
     * 发布单位--检索结果
     */
    private String releaseunit;
    /**
     * 截止时间
     */
    private String endTime;

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
     * @return: java.lang.String 客户端(PAD)
     */
    public String getPadId() {
        return this.padId;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 客户端(PAD)
     */
    public void setPadId(String padId) {
        this.padId = padId;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 分配数量
     */
    public Integer getTaskCount() {
        return this.taskCount;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 分配数量
     */
    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 分配时间
     */
    public String getAssignTime() {
        return this.assignTime;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 分配时间
     */
    public void setAssignTime(String assignTime) {
        this.assignTime = assignTime;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 任务ID
     */
    public String getTaskCode() {
        return this.taskCode;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 任务ID
     */
    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 完成情况
     */
    public String getTaskStatus() {
        return this.taskStatus;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 完成情况
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getReleaseunit() {
        return releaseunit;
    }

    public void setReleaseunit(String releaseunit) {
        this.releaseunit = releaseunit;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        if(ConverterUtil.isNotEmpty(endTime)){
            if(endTime.contains(".")){
                endTime = endTime.substring(0, endTime.indexOf("."));
            }
        }
        this.endTime = endTime;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        this.id = StringUtils.getStrProperty(soapObject, "id");
        this.padId = StringUtils.getStrProperty(soapObject, "padId");
        this.taskCount = StringUtils.getIntProperty(soapObject, "taskCount");
        this.assignTime = StringUtils.getStrProperty(soapObject, "assignTime");
        this.taskCode = StringUtils.getStrProperty(soapObject, "taskCode");
        this.taskStatus = StringUtils.getStrProperty(soapObject, "taskStatus");
        this.releaseunit = StringUtils.getStrProperty(soapObject, "releaseunit");
        this.endTime = StringUtils.getStrProperty(soapObject, "endTime");
    }
}
