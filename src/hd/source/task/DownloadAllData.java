/**
 * Project Name:MainActivity
 * File Name:DownloadAllData.java
 * Package Name:hd.source.task
 * Copyright (c) 2014, doumi-tech.com All Rights Reserved.
 */

package hd.source.task;

import hd.produce.security.cn.HdCnApp;
import hd.produce.security.cn.data.LinkInfo;
import hd.produce.security.cn.data.MonitoringBreedEntity;
import hd.produce.security.cn.data.MonitoringProjectEntity;
import hd.produce.security.cn.data.MonitoringTaskDetailsEntity;
import hd.produce.security.cn.data.MonitoringTaskEntity;
import hd.produce.security.cn.data.SampleInfoEntity;
import hd.produce.security.cn.db.DatabaseManager;
import hd.produce.security.cn.db.SQLManager;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.utils.cn.Constants;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.DownloadBroadcast;
import hd.utils.cn.LogUtil;
import hd.utils.cn.NetworkControl;
import hd.utils.cn.Utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * 下载所有数据
 */
public class DownloadAllData {
	public static final String TAG = "DownloadAllData";
	public static final int SUCCESS = 200;
	public static final int FAIL = 201;
	public static final int EXCEPTION = 202;
	public static final int NO_DATA = 203;
	private static final int SMALL_DOWNLOAD_PERCENT = 20;
	private static final int MIDDLE_DOWNLOAD_PERCENT = 30;
	private static final int MAX_DOWNLOAD_PERCENT = 40;

	private static DownloadAllData sInstance;
	private Context mContext;
	private boolean mIsDownloading = false;
	private CompleteCallback mCompleteCallback;
	// private ProgressDialog mProgressDialog;
	private ArrayList<MonitoringTaskEntity> mTaskList;

	private String mPadId;
	private String mOrgCode;
	private MyThread myThread;

	private DownloadAllData(Context context) {
		mContext = context;
	}

	public static DownloadAllData getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DownloadAllData(context);
		}
		return sInstance;
	}

	/**
	 * 设置下载完后的回调函数
	 */
	public void setCompleteCallback(CompleteCallback callback) {
		mCompleteCallback = callback;
	}

	public boolean isDownloading() {
		return mIsDownloading;
	}

	/**
	 * 开始下载数据
	 */
	public void download() {
		if (mIsDownloading) {
			return;
		}
		try {
			mIsDownloading = true;
			mOrgCode = DataManager.getInstance().getOrgCode();
			mPadId = DataManager.getInstance().getPadId();
			boolean haveNet = NetworkControl.checkNet(mContext);
			if (haveNet) {
				DownloadBroadcast.sendBroadcast(0, "正在使用服务器收集数据....", false);
				// 手动制作下载进度因为下载是比特字符串，所以不能作出正常样的进度，所以手动控制一下
				myThread = new MyThread();
				myThread.setFlag(true);
				myThread.start();
				// 下载数据库文件
				TaskHelper.getDatabase(mContext, listener, 1000 * 60 * 3);
			} else {
				// 没有网络提示
				mLoadCompleteHandler.sendEmptyMessage(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mIsDownloading = false;
		}
	}

	private class MyThread extends Thread {

		private boolean flag;

		public void setFlag(boolean flag) {
			synchronized (MyThread.class) {
				this.flag = flag;
			}
		}

		@Override
		public void run() {
			try {
				int downTime = 0;
				while (flag) {
					downTime += 1;
					DownloadBroadcast.sendBroadcast(1, null, false);
					if (downTime <= SMALL_DOWNLOAD_PERCENT) {
						Thread.sleep(500L);
					} else if (downTime > SMALL_DOWNLOAD_PERCENT
							&& downTime <= MIDDLE_DOWNLOAD_PERCENT) {
						Thread.sleep(1000L);
					} else {
						Thread.sleep(2000L);
					}
					if (downTime == MAX_DOWNLOAD_PERCENT) {
						flag = false;
					}
				}
			} catch (InterruptedException e) {
				LogUtil.e(TAG, "Error in download", e);
			}
		}
	}

	public boolean downFile(String url) throws IOException {
		DownloadBroadcast.sendBroadcast(0, "正在下载服务器数据....", false);
		// 获取文件名
		url = WebServiceUtil.HOST_URL + url.substring(url.indexOf(WebServiceUtil.UPLOAD_PATH));
		URL myURL = new URL(url);
		URLConnection conn = myURL.openConnection();
//		conn.setRequestProperty("Accept-Encoding", "identity");
		conn.setRequestProperty("Accept-Encoding", "gzip");
		conn.connect();
		InputStream is = conn.getInputStream();
//		int fileSize = conn.getContentLength();// 根据响应获取文件大小
//		if (fileSize <= 0 || is == null) {
//			return false;
//		}
		// 按照10%计算1%的大小
//		float filePrecent = fileSize / 10;

		File database = SQLManager.getDatabaseFile();
		// 检测路径是否存在
		File parentPath = new File(database.getParent());
		// 不存在就创建
		if (!parentPath.exists()) {
			parentPath.mkdirs();
		}
		File newDbFile = new File(database.getParent() + "/" + "tempDb.db");
		FileOutputStream fos = new FileOutputStream(newDbFile);
		// 把数据存入路径+文件名
		byte buf[] = new byte[1024];
//		int downLoadFileSize = 0;
//		int totalPre = 0;
		do {
			// 循环读取
			int numread = is.read(buf);
			if (numread == -1) {
				break;
			}
			fos.write(buf, 0, numread);
//			downLoadFileSize += numread;
//			int pre = Math.round(downLoadFileSize * 100 / filePrecent);
//			totalPre += pre;
//			if (totalPre <= 10) {
//				// 更新进度条
//				DownloadBroadcast.sendBroadcast(pre, null, false);
//			}
		} while (true);
		DownloadBroadcast.sendBroadcast(100, null, false);
		try {
			is.close();
		} catch (Exception ex) {
			Log.e("tag", "error: " + ex.getMessage(), ex);
		}
		return true;
	}

	private ITaskListener listener = new ITaskListener() {

		@Override
		public void onTaskFinish(int result, int request, Object entity) {
			if (result == ITaskListener.RESULT_OK) {
				String file = ((SoapObject) entity).getPropertyAsString(0);
				if (ConverterUtil.isNotEmpty(file)) {
					DataManager.getInstance().cleanProject();
					try {
						if (downFile(file)) {
							// 设置当前总进度为50%
							DownloadBroadcast.sendBroadcast(50,
									"下载服务器数据完成....", true);
							// 备份本地抽样单
							DataManager.getInstance().backUpSamples();
							DownloadBroadcast.sendBroadcast(0,
									"正在覆写本地数据库,请不要强制关闭程序....", false);
							// 覆盖本地数据库文件并重新取得连接
							SQLManager.getInstance().overWriteDatabase();
							DownloadBroadcast.sendBroadcast(10, "覆写数据库成功....",
									false);
							try {
								SoapObject webContext = WebServiceUtil
										.callWebService(WebServiceUtil
												.getWebContextPath());
								String webContextPath = WebServiceUtil
										.decodeWebContextPath(webContext);
								Utils.savePreference(HdCnApp.getApplication()
										.getApplicationContext(),
										Constants.SP_KEY_WEBCONTEXTPATH,
										webContextPath);
							} catch (EOFException e) {
								LogUtil.e(TAG, "Error in onTaskFinish", e);
							}
							// 恢复本地未完成抽样单
							DataManager.getInstance().recoverSamples();
							// 下载所有抽样单图片
							DataManager.getInstance().downLoadServerImages();
							// // 展开所有受检单位到缓存
							// DataManager.getInstance().extendsMonintringSite();
							DownloadBroadcast.sendBroadcast(20, "数据下载完成....",
									false);
						}
					} catch (IOException e1) {
						LogUtil.e(TAG, "Error in onTaskFinish", e1);
					}
				}
				// 所有数据都已经下载完了
				mLoadCompleteHandler.sendEmptyMessage(SUCCESS);
			} else if (result == ITaskListener.RESULT_NET_ERROR) {
				mLoadCompleteHandler.sendEmptyMessage(FAIL);
				// DownloadBroadcast.sendBroadcast(100, "数据下载出错....", true);
				// 所有异常都按照网络异常处理
				DownloadBroadcast.sendBroadcast(100, "网络连接异常,请重试....", true);
			} else {
				mLoadCompleteHandler.sendEmptyMessage(NO_DATA);
				DownloadBroadcast.sendBroadcast(100, "服务器上没有对应数据....", true);
			}
		}

	};
	private ITaskListener mProjectListListener = new ITaskListener() {

		@Override
		public void onTaskFinish(int result, int request, Object entity) {
			if (result == ITaskListener.RESULT_OK) {
				WebServiceUtil.getSysMonitoringArea((SoapObject) entity);
				SoapObject siteAction = WebServiceUtil
						.getAllMonitoringSiteRequest();
				SoapObject siteResult = null;
				long start = System.currentTimeMillis();
				try {
					siteResult = WebServiceUtil.callWebService(siteAction);
					long endServer = System.currentTimeMillis();
					LogUtil.e("下载耗时:" + (endServer - start) / 1000 + "秒");
				} catch (EOFException e1) {
					LogUtil.e(
							TAG,
							"Error in onTaskFinish.getAllMonitoringSiteRequest",
							e1);
				}
				// 下载受检单位
				WebServiceUtil.getAllMonitoringSite(siteResult);
				try {
					// 下载所有项目
					SoapObject proRequest = WebServiceUtil
							.callWebService(WebServiceUtil
									.getProjectInfoRequest(mOrgCode, mPadId, ""));
					List<TbMonitoringProject> results = WebServiceUtil
							.getProjectInfo(proRequest);
					if (results == null || results.isEmpty()) {
						mLoadCompleteHandler.sendEmptyMessage(NO_DATA);
						return;
					}
					DataManager.getInstance().cleanProject();
					// 设置到Map中不用再次取
					DataManager.getInstance().setMonitoringProjectEntityList(
							results);
					for (TbMonitoringProject project : results) {
						int type = Integer.valueOf(project.getType());
						switch (type) {
						case 1:
							DataManager.getInstance()
									.getRoutinemonitoringEntityList()
									.add(project);
							break;
						case 2:
							DataManager.getInstance()
									.getGeneralcheckEntityList().add(project);
							break;
						case 3:
							DataManager.getInstance()
									.getSpecialTaskEntityList().add(project);
							break;
						case 4:
							DataManager.getInstance()
									.getSuperviseCheckEntityList().add(project);
							break;
						default:
							break;
						}
					}
					// 下载完所有项目，然后分别把每个项目的所有taskList下载下来
					downloadTaskList(results);
				} catch (Exception e) {
					LogUtil.e(TAG, "Error in onTaskFinish.getProjectList", e);
					mLoadCompleteHandler.sendEmptyMessage(EXCEPTION);
				}
			} else if (result == ITaskListener.RESULT_NET_ERROR) {
				mLoadCompleteHandler.sendEmptyMessage(FAIL);
			} else {
				mLoadCompleteHandler.sendEmptyMessage(NO_DATA);
			}
		}

	};

	/**
	 * 把项目下的所有taskList下载下来
	 */
	private void downloadTaskList(List<TbMonitoringProject> projectList) {
		if (projectList == null || projectList.isEmpty()) {// 如果当前没有项目
			return;
		}
		DatabaseManager.getInstance().truncate(
				DatabaseManager.TB_MONITORING_TASK);
		DatabaseManager.getInstance().truncate(
				DatabaseManager.TB_MONITORING_TASK_DETAILS);
		DatabaseManager.getInstance().truncate(
				DatabaseManager.TB_MONITORING_BREED);
		int count = projectList.size();
		for (int i = 0; i < count; i++) {
			MonitoringProjectEntity paramEntity = new MonitoringProjectEntity();
			paramEntity.setProjectCode(projectList.get(i).getProjectCode());
			paramEntity.setPadId(mPadId);
			SoapObject request = WebServiceUtil
					.getTaskInfoListRequest(paramEntity);
			if (!Utils.isNetValid(mContext)) {// 没有网络
				mLoadCompleteHandler.sendEmptyMessage(EXCEPTION);
				return;
			} else {
				Object result = null;
				if (request != null) {
					try {
						result = WebServiceUtil.callWebService(request);
					} catch (EOFException e) {
						e.printStackTrace();
					}
				}
				if (result != null) {
					// 下载成功，保存结果,然后继续
					List<MonitoringTaskEntity> taskList = WebServiceUtil
							.getTaskInfoList((SoapObject) result);
					DataManager.getInstance().getTaskEntityListMap()
							.put(projectList.get(i).getProjectCode(), taskList);
					if (mTaskList == null) {
						mTaskList = new ArrayList<MonitoringTaskEntity>();
					}
					mTaskList.addAll(taskList);
				} else {
					// 下载失败,退出
					mLoadCompleteHandler.sendEmptyMessage(FAIL);
					return;
				}
			}
		}

		// 所有taskList都下载完了，该下载每个任务的具体信息了
		downTaskInfo();
	}

	/**
	 * 下载每个任务的详细信息
	 */
	private void downTaskInfo() {
		int count = mTaskList.size();
		for (int i = 0; i < count; i++) {
			String taskCode = mTaskList.get(i).getTaskcode();
			String projectCode = mTaskList.get(i).getProjectCode();
			TbMonitoringProject projectEntity = DataManager.getInstance()
					.getMonitoringProjectEntity(projectCode);
			// //1.下载任务的抽样品种
			// if(!downloadMonitoringBreed(taskCode, projectCode)) {
			// return;
			// }
			//
			// //2.下载任务的抽样地区
			// if(!downloadMonitoringArea(taskCode)) {
			// return;
			// }
			//
			// //3.下载任务的抽样环节
			// if(!downloadLinkInfo(projectCode,
			// projectEntity.getIndustryCode())) {
			// return;
			// }
			//
			// //4.下载任务的详细信息
			// if(!downloadInfo(taskCode)) {
			// return;
			// }
			//
			// 5.下载任务的已完成数量
			if (!downloadFinishSamplingInfo(taskCode,
					getSampleType(projectEntity), projectCode)) {
				return;
			}
			//
			// //6.样品名称
			// if(!downloadSampleName(projectCode)) {
			// return;
			// }
		}
		// 所有数据都已经下载完了
		mLoadCompleteHandler.sendEmptyMessage(SUCCESS);
	}

	/**
	 * 下载任务的已完成数量
	 * 
	 * @param taskCode
	 * @param sampleType
	 * @param projectCode
	 * @return
	 */
	private boolean downloadFinishSamplingInfo(String taskCode,
			String sampleType, String projectCode) {
		SoapObject request = WebServiceUtil.getFinishSamplingInfoRequest(
				mPadId, taskCode, sampleType, projectCode);
		if (!Utils.isNetValid(mContext)) {// 没有网络
			mLoadCompleteHandler.sendEmptyMessage(EXCEPTION);
			return false;
		} else {
			Object result = null;
			if (request != null) {
				try {
					result = WebServiceUtil.callWebService(request);
				} catch (EOFException e) {
					e.printStackTrace();
					// 下载失败,退出
					mLoadCompleteHandler.sendEmptyMessage(FAIL);
					return false;
				}
			}
			if (result != null) {
				// 下载成功，保存结果,然后继续
				List<SampleInfoEntity> doneResult = WebServiceUtil
						.getFinishSamplingInfo((SoapObject) result);
				DataManager.getInstance().getFinishSampleCount()
						.put(taskCode, doneResult.size());
			}
		}
		return true;
	}

	/**
	 * 下载任务的详细信息
	 * 
	 * @param taskCode
	 * @return
	 */
	private boolean downloadInfo(String taskCode) {
		SoapObject request = WebServiceUtil
				.getTaskInfoRequest(taskCode, mPadId);
		if (!Utils.isNetValid(mContext)) {// 没有网络
			mLoadCompleteHandler.sendEmptyMessage(EXCEPTION);
			return false;
		} else {
			Object result = null;
			if (request != null) {
				try {
					result = WebServiceUtil.callWebService(request);
				} catch (EOFException e) {
					e.printStackTrace();
				}
			}
			if (result != null) {
				// 下载成功，保存结果,然后继续
				MonitoringTaskDetailsEntity taskDetailsEntity = WebServiceUtil
						.getTaskInfo((SoapObject) result);
				DataManager.getInstance().getMonitoringTaskDetailsMap()
						.put(taskCode, taskDetailsEntity);
				return true;
			} else {
				// 下载失败,退出
				mLoadCompleteHandler.sendEmptyMessage(FAIL);
				return false;
			}
		}
	}

	/**
	 * 下载任务的抽样环节
	 * 
	 * @param industryCode
	 * @return
	 */
	private boolean downloadLinkInfo(String projectCode, String industryCode) {
		SoapObject request = WebServiceUtil.getLinkInfoRequest(industryCode);
		if (!Utils.isNetValid(mContext)) {// 没有网络
			mLoadCompleteHandler.sendEmptyMessage(EXCEPTION);
			return false;
		} else {
			Object result = null;
			if (request != null) {
				try {
					result = WebServiceUtil.callWebService(request);
				} catch (EOFException e) {
					e.printStackTrace();
				}
			}
			if (result != null) {
				// 下载成功，保存结果,然后继续
				List<LinkInfo> linkResult = WebServiceUtil
						.getLinkInfo((SoapObject) result);
				DataManager.getInstance().getLinkInfoListMap()
						.put(projectCode, linkResult);
				return true;
			} else {
				// 下载失败,退出
				mLoadCompleteHandler.sendEmptyMessage(FAIL);
				return false;
			}
		}
	}

	/**
	 * 下载任务的抽样地区
	 * 
	 * @param taskCode
	 * @return
	 */
	private boolean downloadMonitoringArea(String taskCode) {
		SoapObject request = WebServiceUtil.getMonitoringAreaRequest(taskCode);
		if (!Utils.isNetValid(mContext)) {// 没有网络
			mLoadCompleteHandler.sendEmptyMessage(EXCEPTION);
			return false;
		} else {
			Object result = null;
			if (request != null) {
				try {
					result = WebServiceUtil.callWebService(request);
				} catch (EOFException e) {
					e.printStackTrace();
				}
			}
			if (result != null) {
				// 下载成功，保存结果,然后继续
				List<String> areaResult = WebServiceUtil
						.getMonitoringAreaForTask((SoapObject) result);
				DataManager.getInstance().getAreaListMap()
						.put(taskCode, areaResult);
				return true;
			} else {
				// 下载失败,退出
				mLoadCompleteHandler.sendEmptyMessage(FAIL);
				return false;
			}
		}
	}

	/**
	 * 下载任务的抽样品种
	 * 
	 * @param taskCode
	 * @param projectCode
	 * @return
	 */
	private boolean downloadMonitoringBreed(String taskCode, String projectCode) {
		SoapObject request = WebServiceUtil.getMonitoringBreedForTaskRequest(
				taskCode, projectCode);
		if (!Utils.isNetValid(mContext)) {// 没有网络
			mLoadCompleteHandler.sendEmptyMessage(EXCEPTION);
			return false;
		} else {
			Object result = null;
			if (request != null) {
				try {
					result = WebServiceUtil.callWebService(request);
				} catch (EOFException e) {
					e.printStackTrace();
					// 下载失败,退出
					mLoadCompleteHandler.sendEmptyMessage(FAIL);
					return false;
				}
			}
			if (result != null) {
				// 下载成功，保存结果,然后继续
				List<MonitoringBreedEntity> breedList = WebServiceUtil
						.getMonitoringBreedForTask((SoapObject) result,
								projectCode);
				ArrayList<String> breadStrList = WebServiceUtil
						.getMonitoringBreadStr(breedList);
				ArrayList<String> breadAgrCodeList = WebServiceUtil
						.getMonitoringBreadAgrCode(breedList);

				DataManager.getInstance().getBreadListMap()
						.put(taskCode, breedList);
				DataManager.getInstance().getBreadStrListMap()
						.put(taskCode, breadStrList);
				DataManager.getInstance().getBreadAgrCodeListMap()
						.put(taskCode, breadAgrCodeList);
			}
		}
		return true;
	}

	/**
	 * 下载任务的抽样品种
	 * 
	 * @param taskCode
	 * @param projectCode
	 * @return
	 */
	private boolean downloadSampleName(String projectCode) {
		SoapObject request = WebServiceUtil
				.getSampleNameForTaskRequest(projectCode);
		if (!Utils.isNetValid(mContext)) {// 没有网络
			mLoadCompleteHandler.sendEmptyMessage(EXCEPTION);
			return false;
		} else {
			Object result = null;
			if (request != null) {
				try {
					result = WebServiceUtil.callWebService(request);
				} catch (EOFException e) {
					e.printStackTrace();
					// 下载失败,退出
					mLoadCompleteHandler.sendEmptyMessage(FAIL);
					return false;
				}
			}
			if (result != null) {
				// 下载成功，保存结果,然后继续
				List<MonitoringBreedEntity> nameList = WebServiceUtil
						.getMonitoringBreedForTask((SoapObject) result,
								projectCode);
				ArrayList<String> nameStrList = WebServiceUtil
						.getMonitoringBreadStr(nameList);
				ArrayList<String> nameAgrCodeList = WebServiceUtil
						.getMonitoringBreadAgrCode(nameList);

				DataManager.getInstance().getSampleNameListMap()
						.put(projectCode, nameList);
				DataManager.getInstance().getSampleNameStrListMap()
						.put(projectCode, nameStrList);
				DataManager.getInstance().getSampleNameAgrCodeListMap()
						.put(projectCode, nameAgrCodeList);
			}
		}
		return true;
	}

	private Handler mLoadCompleteHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			mIsDownloading = false;
			// if(mProgressDialog != null && mProgressDialog.isShowing()) {
			// mProgressDialog.dismiss();
			// }
			// switch(msg.what) {
			// case SUCCESS:
			// Toast.makeText(mContext,
			// mContext.getText(R.string.msg_load_data),
			// Toast.LENGTH_LONG).show();
			// break;
			// case FAIL:
			// Toast.makeText(mContext,
			// mContext.getText(R.string.msg_load_no_data),
			// Toast.LENGTH_LONG).show();
			// break;
			// case EXCEPTION:
			// Toast.makeText(mContext,
			// mContext.getText(R.string.msg_load_faild),
			// Toast.LENGTH_LONG).show();
			// break;
			// }
			if (mCompleteCallback != null) {
				mCompleteCallback.downloadCompleted(msg.what);
			}
			return false;
		}

	});

	/**
	 * 获得任务的具体类型
	 * 
	 * @param projectEntity
	 * @return 任务的具体类型
	 */
	private String getSampleType(TbMonitoringProject projectEntity) {
		int monitorType = -1;
		try {
			// type的解释
			// 0:例行检测
			// 1;风险 也叫普查
			// 2:监督抽查
			// 3：专项
			// 4：生鲜乳
			int type = Integer.valueOf(projectEntity.getSampleTemplet());
			String industryCode = projectEntity.getIndustryCode();
			if (TextUtils.equals(industryCode, "a")) {// 畜禽业
				switch (type) {
				case 0: // 例行检查
				case 1:
				case 3:
					monitorType = Constants.LIVE_STOCK_SAMPLE;
					break;
				case 2:
					monitorType = Constants.SUPERVISE_SAMPLE;
					break;

				case 4:
					monitorType = Constants.RAW_MILK_SAMPLE;
					break;

				default:
					break;
				}
			} else if (TextUtils.equals(industryCode, "f")) {// 种植业 应该是没有4
				switch (type) {
				case 0: // 例行检查
					monitorType = Constants.ROUTINE_SAMPLE_TYPE;
					break;
				case 1:
					monitorType = Constants.RISK_SAMPLE;
					break;
				case 2:
					monitorType = Constants.SUPERVISE_SAMPLE;
					break;

				case 3:
					// 农产品的专项跳转随意抽样
					monitorType = Constants.SUPERVISE_SAMPLE;
					break;
				/**
				 * case 4: sampleClass = RAW_MILK_SAMPLE; break;
				 */
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(monitorType);
	}

	public interface CompleteCallback {

		public void downloadCompleted(int code);

	}
}
