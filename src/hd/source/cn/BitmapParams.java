
package hd.source.cn;

import hd.utils.cn.Utils;

public class BitmapParams {

    public static final int CAPTURE_TYPE_AVATAR = 1;

    public static final int CAPTURE_TYPE_MESSAGE = 2;

    public static final int CAPTURE_TYPE_WALL_HEADER = 3;

    public static final int CAPTURE_TYPE_PERSON_HEADER = 4;

    public static final int CAPTURE_TYPE_NO_CROP = 5;

    public static final int LOAD_TYPE_AVATAR = 1;

    public static final int LOAD_TYPE_AVATAR_EXO = LOAD_TYPE_AVATAR + 1;

    public static final int LOAD_TYPE_IMAGE_THUMB = LOAD_TYPE_AVATAR_EXO + 1;

    public static final int LOAD_TYPE_RADIO = LOAD_TYPE_IMAGE_THUMB + 1;

    public static final int LOAD_TYPE_LIST_LIKE = LOAD_TYPE_RADIO + 1;

    public static final int LOAD_TYPE_LIST_LIKE_SJ = LOAD_TYPE_LIST_LIKE + 1;

    public static final int LOAD_TYPE_LIST_MSG = LOAD_TYPE_LIST_LIKE_SJ + 1;

    public static final int LOAD_TYPE_LIST_MSG_SJ = LOAD_TYPE_LIST_MSG + 1;

    public static final int LOAD_TYPE_LIST_MSG_EXO = LOAD_TYPE_LIST_MSG_SJ + 1;

    public static final int LOAD_TYPE_CONTENT_MSG = LOAD_TYPE_LIST_MSG_EXO + 1;

    public static final int LOAD_TYPE_CONTENT_TRACE = LOAD_TYPE_CONTENT_MSG + 1;

    public static final int LOAD_TYPE_ORG = LOAD_TYPE_CONTENT_TRACE + 1;

    public static final int LOAD_IMAGE_SHOW = LOAD_TYPE_ORG + 1;

    static final int PADDIND_LIST_MSG = 28;

    static final int PADDIND_LIST_MSG_SJ = 12;

    static final int PADDIND_LIST_MSG_EXO = 36;

    static final int PADDIND_CONTENT_MSG = 12;

    static final int PADDIND_CONTENT_TRACE = 30;

    float round = 0.0f;

    int targetW;

    int targetH;

    int mType;

    boolean needResize = false;

    public BitmapParams(int type, boolean resize) {
        this(type);
        needResize = resize;
    }

    public BitmapParams(int type, int width, int height) {
        this(type);
        targetW = width;
        targetH = height;
    }

    public BitmapParams(int type) {
        mType = type;
        switch (type) {
            case LOAD_TYPE_AVATAR:
                round = 0.04f;
                targetW = 160;
                targetH = 160;
                break;
            case LOAD_TYPE_AVATAR_EXO:
                round = 0.0f;
                targetW = 160;
                targetH = 160;
                break;
            case LOAD_TYPE_LIST_LIKE:
                round = 0.04f;
                targetW = Utils.getScreenWidth();
                targetH = Utils.getScreenWidth();
                break;
            case LOAD_TYPE_LIST_LIKE_SJ:
                targetW = Utils.getScreenWidth();
                targetH = Utils.getScreenWidth();
                break;
            case LOAD_TYPE_IMAGE_THUMB:
                round = 0.0f;
                targetW = 180;
                targetH = 180;
                break;
            case LOAD_TYPE_RADIO:
                round = 0.04f;
                targetW = 120;
                targetH = 120;
                break;
            case LOAD_TYPE_ORG:
                round = 0.0f;
                targetW = Utils.getScreenWidth() * 3 / 2;
                targetH = Utils.getScreenHeight() * 3 / 2;
                break;
            case LOAD_TYPE_LIST_MSG:
            case LOAD_TYPE_LIST_MSG_EXO:
            case LOAD_IMAGE_SHOW:
            case LOAD_TYPE_CONTENT_MSG:
            case LOAD_TYPE_CONTENT_TRACE:
            case LOAD_TYPE_LIST_MSG_SJ:
                needResize = true;
                round = 0.0f;
                targetW = Utils.getScreenWidth();
                targetH = Utils.getScreenHeight();
                break;

            default:
                break;
        }
    }
}
