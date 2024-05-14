package hd.utils.cn;

import hd.produce.security.cn.FillLiveStockSampleAct;
import hd.produce.security.cn.FillRawMilkSampleActivity;
import hd.produce.security.cn.FillRiskSampleActivity;
import hd.produce.security.cn.FillRoutineSampleActivity;
import hd.produce.security.cn.FillSuperviseSampleActivity;

public class CommonUtils {
    /** 例行监测 */
    private final static int ROUTINE_SAMPLE_TYPE = 1;

    /** 监督、专项 */
    private final static int SUPERVISE_SAMPLE = 2;

    /** 风险(普查) */
    private final static int RISK_SAMPLE = 3;

    /** 生鲜乳 */
    private final static int RAW_MILK_SAMPLE = 4;

    /** 畜禽 */
    private final static int LIVE_STOCK_SAMPLE = 5;

    /**
     * 取得抽样单对应的activity
     * 
     * @param templete
     *            项目表的templete字段
     * @return
     */
    public static Class<?> getSampleTemplete(String templete) {
        Class<?> cls = null;

        int tem = ConverterUtil.toInteger(templete, 1);

        switch (tem) {
        // 例行监测
        case ROUTINE_SAMPLE_TYPE:
            cls = FillRoutineSampleActivity.class;
            break;
        // 监督、专项
        case SUPERVISE_SAMPLE:
            cls = FillSuperviseSampleActivity.class;
            break;
        // 风险(普查)
        case RISK_SAMPLE:
            cls = FillRiskSampleActivity.class;
            break;
        // 生鲜乳
        case RAW_MILK_SAMPLE:
            cls = FillRawMilkSampleActivity.class;
            break;
        // 畜禽
        case LIVE_STOCK_SAMPLE:
            cls = FillLiveStockSampleAct.class;
            break;
        default:
            break;
        }
        return cls;
    }
}
