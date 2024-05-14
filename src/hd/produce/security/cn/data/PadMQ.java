package hd.produce.security.cn.data;

import java.util.List;

public class PadMQ {
    private List<PadforwardInfo> padforwardInfoS;
    private List<PadTCInfo> PadTCInfoS;
    private List<PadforwardInfo> padforwardInfoSuccess;

    public List<PadforwardInfo> getPadforwardInfoS() {
        return padforwardInfoS;
    }

    public void setPadforwardInfoS(List<PadforwardInfo> padforwardInfoS) {
        this.padforwardInfoS = padforwardInfoS;
    }

    public List<PadTCInfo> getPadTCInfoS() {
        return PadTCInfoS;
    }

    public void setPadTCInfoS(List<PadTCInfo> padTCInfoS) {
        PadTCInfoS = padTCInfoS;
    }

    public List<PadforwardInfo> getPadforwardInfoSuccess() {
        return padforwardInfoSuccess;
    }

    public void setPadforwardInfoSuccess(List<PadforwardInfo> padforwardInfoSuccess) {
        this.padforwardInfoSuccess = padforwardInfoSuccess;
    }
}
