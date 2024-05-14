package hd.source.task;

public interface ITaskListener {

    int RESULT_OK = 0;

    int RESULT_FAILED = RESULT_OK + 1;

    int RESULT_CANCEL = RESULT_FAILED + 1;

    int RESULT_NET_ERROR = RESULT_CANCEL + 1;

    int RESULT_NOT_DATA = RESULT_NET_ERROR + 1;

    void onTaskFinish(int result, int request, Object entity);
}
