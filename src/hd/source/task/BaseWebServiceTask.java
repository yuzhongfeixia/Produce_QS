package hd.source.task;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.content.Context;

import hd.source.cn.AsyncTask;
import hd.utils.cn.Constants;
import hd.utils.cn.Utils;

import java.io.EOFException;

public class BaseWebServiceTask extends AsyncTask<ITaskListener, Integer, Integer> {

    protected SoapObject mRequest = null;

    protected SoapSerializationEnvelope mEnvelope = null;

    protected int mRequestCode;

    protected int timeOut = 0;

    protected ITaskListener mListener;

    protected Object mResult = null;

    protected Context mcontext;

    public BaseWebServiceTask(Context context, SoapObject request, int requestCode, int timeOut) {
        this.mcontext = context;
        this.mRequest = request;
        this.mRequestCode = requestCode;
        this.timeOut = timeOut;
    }

    public BaseWebServiceTask(Context context, SoapObject request, int requestCode) {
        mcontext = context;
        mRequest = request;
        mRequestCode = requestCode;
    }

    public BaseWebServiceTask(Context context, SoapSerializationEnvelope envelope, int requestCode) {
        mcontext = context;
        mEnvelope = envelope;
        mRequestCode = requestCode;
    }

    protected void setListnerResult(int result) {
        if (null != mListener) {
            mListener.onTaskFinish(result, mRequestCode, mResult);
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
        if (!Utils.isNetValid(mcontext)) {
            mResult = Constants.NET_NONE;
            setListnerResult(ITaskListener.RESULT_NET_ERROR);
            return 0;
        } else {
            if (mEnvelope != null) {
                mResult = WebServiceUtil.callWebService(mEnvelope);
            }
            if (mRequest != null) {
                try {
                    if (timeOut == 0) {
                        timeOut = Constants.HTTP_SO_TIMEOUT;
                    }
                    mResult = WebServiceUtil.callWebService(mRequest, timeOut);
                } catch (EOFException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (mResult != null) {
                setListnerResult(ITaskListener.RESULT_OK);
            } else {
                setListnerResult(ITaskListener.RESULT_FAILED);
            }
            return 0;
        }
    }

}
