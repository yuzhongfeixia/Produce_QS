package hd.produce.security.cn.data;

import java.io.Serializable;

/**
 * 二维码信息
 *
 * @author liuhanhan512
 * @Package hd.produce.security.cn.data
 * @ClassName: BarCodeInfo
 * @mail 470209697@qq.com
 * @date 2013-11-29 上午10:10:08
 */
public class BarCodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String formatText;

    private String typeText;

    private String formattedTime;

    private int metaDataLength;

    private String metaDataText;

    private String displayContent;

    private int scaledSize;

    public void setFormatText(String formatText) {
        this.formatText = formatText;
    }

    public String getFormatText() {
        return formatText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setMetaDataLength(int metaDataLength) {
        this.metaDataLength = metaDataLength;
    }

    public int getMetaDataLength() {
        return metaDataLength;
    }

    public void setMetaDataText(String metaDataText) {
        this.metaDataText = metaDataText;
    }

    public String getMetaDataText() {
        return metaDataText;
    }

    public void setDisplayContent(String displayContent) {
        this.displayContent = displayContent;
    }

    public String getDisplayContent() {
        return displayContent;
    }

    public void setScaledSize(int scaledSize) {
        this.scaledSize = scaledSize;
    }

    public int getScaledSize() {
        return scaledSize;
    }

}
