package hd.produce.security.cn.entity;

import hd.produce.security.cn.annotation.Except;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.StringUtils;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

public class TbMonitoringProject implements java.io.Serializable {
    /**
     * 序列ID
     */
    private static final long serialVersionUID = -4762292090132380910L;

    // ID
    private String id;
    // 方案ID
    private String planCode;
    // 项目名称
    private String name;
    // 牵头单位编号
    private String leadunit;
    // 监测开始时间
    private String starttime;
    // 监测结束时间
    private String endtime;
    // 是否抽检分离
    private String detached;
    // 项目状态
    private String state;
    // 项目ID
    private String projectCode;
    // 发布时间
    private String publishDate;
    // 抽样单模板
    private String sampleTemplet;
    // 行业
    private String industryCode;
    // 整拆包分发FLG
    private Integer packingFlg;
    // 机构名称
    private String ogrname;
    // 方案级别
    private String plevel;
    // 监测类型
    private String type;
    // 任务详情的抽样品种(字符串)
    private String projectBree;
    // 任务详情的抽样环节(字符串)
    private String linkInfo;
    // 抽样单模板
    private String templete;
    // 抽样单的抽样环节 (list)
    @Except
    private List<TbMonitoringLinkInfo> linkInfoList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeadunit() {
        return leadunit;
    }

    public void setLeadunit(String leadunit) {
        this.leadunit = leadunit;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getDetached() {
        return detached;
    }

    public void setDetached(String detached) {
        this.detached = detached;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getSampleTemplet() {
        return sampleTemplet;
    }

    public void setSampleTemplet(String sampleTemplet) {
        this.sampleTemplet = sampleTemplet;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public Integer getPackingFlg() {
        return packingFlg;
    }

    public void setPackingFlg(Integer packingFlg) {
        this.packingFlg = packingFlg;
    }

    public String getOgrname() {
        return ogrname;
    }

    public void setOgrname(String ogrname) {
        this.ogrname = ogrname;
    }

    public String getPlevel() {
        return plevel;
    }

    public void setPlevel(String plevel) {
        this.plevel = plevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectBree() {
        return projectBree;
    }

    public void setProjectBree(String projectBree) {
        this.projectBree = projectBree;
    }

    public String getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(String linkInfo) {
        this.linkInfo = linkInfo;
    }

    public List<TbMonitoringLinkInfo> getLinkInfoList() {
        return linkInfoList;
    }

    public void setLinkInfoList(List<TbMonitoringLinkInfo> linkInfoList) {
        this.linkInfoList = linkInfoList;
    }

    public String getTemplete() {
        return templete;
    }

    public void setTemplete(String templete) {
        this.templete = templete;
    }

    /**
     * parseBySoapObject
     */
    public void parseBySoapObject(SoapObject soapObject) {
        this.id = StringUtils.getStrProperty(soapObject, "id");
        if (ConverterUtil.isEmpty(this.id)) {
            this.id = ConverterUtil.getUUID();
        }
        this.projectCode = StringUtils.getStrProperty(soapObject, "projectCode");
        this.industryCode = StringUtils.getStrProperty(soapObject, "industryCode");
        this.type = StringUtils.getStrProperty(soapObject, "type");
        this.packingFlg = StringUtils.getIntProperty(soapObject, "packingFlg");
        this.name = StringUtils.getStrProperty(soapObject, "name");
        this.plevel = StringUtils.getStrProperty(soapObject, "plevel");
        this.sampleTemplet = StringUtils.getStrProperty(soapObject, "sampleTemplet");
        this.projectBree = StringUtils.getStrProperty(soapObject, "projectBreed");
        this.templete = StringUtils.getStrProperty(soapObject, "templete");
        if (soapObject.hasProperty("linkInfoList")) {
            linkInfoList = new ArrayList<TbMonitoringLinkInfo>();
            for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                SoapObject soapLink;
                try {
                    soapLink = (SoapObject) soapObject.getProperty(i);
                } catch (Exception e) {
                    continue;
                }
                TbMonitoringLinkInfo linkInfo = new TbMonitoringLinkInfo();
                linkInfo.parseBySoapObject(soapLink);
                linkInfo.setProjectId(this.projectCode);
                linkInfoList.add(linkInfo);
            }
        }
    }
}
