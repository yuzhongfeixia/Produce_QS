package hd.produce.security.cn.entity;

public class TbMonitoringArea implements java.io.Serializable {

    /**
     * 序列ID
     */
    private static final long serialVersionUID = -1476098671086269762L;

    // 地区代码
    private String id;
    // 父地区代码
    private String pid;
    // 地区名称
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
