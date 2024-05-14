package hd.source.task;

import hd.source.cn.AsyncTask;
import hd.utils.cn.Constants;
import hd.utils.cn.Utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class LocalDBTask extends AsyncTask<ITaskListener, Integer, Integer> {

    protected ITaskListener mListener;

    protected Object mResult = null;

    protected int taskMethod;

    protected Map<String, Object> param = new HashMap<String, Object>();

    public LocalDBTask(int taskMethod) {
        this.taskMethod = taskMethod;
    }

    public LocalDBTask(int taskMethod, Map<String, Object> params) {
        this.taskMethod = taskMethod;
        this.param = params;
        if (null == this.param) {
            param = new HashMap<String, Object>();
        }
    }

    protected void setListnerResult(int result) {
        if (null != mListener) {
            mListener.onTaskFinish(result, taskMethod, mResult);
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
    }

    @Override
    protected Integer doInBackground(ITaskListener... params) {
        if (params.length == 0 || params[0] == null) {
            return 0;
        }
        mListener = params[0];
        mResult = DataManager.getInstance().getTask(taskMethod, param);
        if (mResult != null) {
            setListnerResult(ITaskListener.RESULT_OK);
        } else {
            setListnerResult(ITaskListener.RESULT_NOT_DATA);
        }
        return 0;
    }

}
