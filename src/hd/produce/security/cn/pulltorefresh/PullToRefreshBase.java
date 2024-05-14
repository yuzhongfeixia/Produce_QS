package hd.produce.security.cn.pulltorefresh;

import hd.produce.security.cn.R;
import hd.utils.cn.Utils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

public abstract class PullToRefreshBase<T extends View> extends FrameLayout
		implements GestureDetector.OnGestureListener {

	public int MAX_MOVE_HEIGHT;
	public int HEAD_HEIGHT;// 为了适应下拉刷新背景图，所以设置的大一些
	public int DELTA_Y;
	public int MAX_LENGHT;

	static final float FRICTION = 2.0f;

	public static final int MODE_PULL_DOWN_TO_REFRESH = 0x1;
	public static final int MODE_PULL_UP_TO_REFRESH = 0x2;
	public static final int MODE_BOTH = 0x3;

	private int mode = MODE_PULL_DOWN_TO_REFRESH;
	private int currentMode;

	private boolean disableScrollingWhileRefreshing = true;

	T refreshableView;
	private boolean isPullToRefreshEnabled = true;

	private LoadingLayout headerLayout;
	private LoadingLayout footerLayout;
	private int headerHeight;

	private OnRefreshListener onRefreshListener;
	private OnSpaceStateChangeListener onSpaceStateChangeListener;

	private String requestUrl;// 请求的url地址，用来当做key得到更新时间
	private boolean isPrivateData;// 请求的url地址是否是私有数据，用来当做key得到更新时间
	private boolean shouldShowUpdataTime;// 是否需要显示更新时间（根据是否需要缓存数据）

	private LoadingLayout mMoniterHeaderLoadingLayout;

	long time = -1L;

	public PullToRefreshBase(Context context) {
		super(context);
		init(context, null);
	}

	public PullToRefreshBase(Context context, int mode) {
		super(context);
		this.mode = mode;
		init(context, null);
	}

	public PullToRefreshBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	/**
	 * Deprecated. Use {@link #getRefreshableView()} from now on.
	 * 
	 * @deprecated
	 * @return The Refreshable View which is currently wrapped
	 */
	public final T getAdapterView() {
		return refreshableView;
	}

	/**
	 * Get the Wrapped Refreshable View. Anything returned here has already been
	 * added to the content view.
	 * 
	 * @return The View which is currently wrapped
	 */
	public final T getRefreshableView() {
		return refreshableView;
	}

	/**
	 * By default the Widget disabled scrolling on the Refreshable View while
	 * refreshing. This method can change this behaviour.
	 * 
	 * @param disableScrollingWhileRefreshing
	 *            - true if you want to disable scrolling while refreshing
	 */
	public final void setDisableScrollingWhileRefreshing(
			boolean disableScrollingWhileRefreshing) {
		this.disableScrollingWhileRefreshing = disableScrollingWhileRefreshing;
	}

	/**
	 * Set OnRefreshListener for the Widget
	 * 
	 * @param listener
	 *            - Listener to be used when the Widget is set to Refresh
	 */
	public final void setOnRefreshListener(OnRefreshListener listener) {
		onRefreshListener = listener;
	}

	public final void setOnSpaceStateChangeListener(
			OnSpaceStateChangeListener listener) {
		onSpaceStateChangeListener = listener;
	}

	/**
	 * A mutator to enable/disable Pull-to-Refresh for the current View
	 * 
	 * @param enable
	 *            Whether Pull-To-Refresh should be used
	 */
	public final void setPullToRefreshEnabled(boolean enable) {
		this.isPullToRefreshEnabled = enable;
	}

	/**
	 * Set Text to show when the Widget is being pulled, and will refresh when
	 * released
	 * 
	 * @param releaseLabel
	 *            - String to display
	 */
	public void setReleaseLabel(String releaseLabel) {
		if (null != headerLayout) {
			headerLayout.setReleaseLabel(releaseLabel);
		}
		if (null != footerLayout) {
			footerLayout.setReleaseLabel(releaseLabel);
		}
	}

	public void setRefreshBackground(int color) {
		headerLayout.setBacBackground(color);
	}

	/**
	 * Set Text to show when the Widget is being Pulled
	 * 
	 * @param pullLabel
	 *            - String to display
	 */
	public void setPullLabel(String pullLabel) {
		if (null != headerLayout) {
			headerLayout.setPullLabel(pullLabel);
		}
		if (null != footerLayout) {
			footerLayout.setPullLabel(pullLabel);
		}
	}

	/**
	 * Set Text to show when the Widget is refreshing
	 * 
	 * @param refreshingLabel
	 *            - String to display
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		if (null != headerLayout) {
			headerLayout.setRefreshingLabel(refreshingLabel);
		}
		if (null != footerLayout) {
			footerLayout.setRefreshingLabel(refreshingLabel);
		}
	}

	protected void addRefreshableView(Context context, T refreshableView) {
		addView(refreshableView, new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/**
	 * This is implemented by derived classes to return the created View. If you
	 * need to use a custom View (such as a custom ListView), override this
	 * method and return an instance of your custom class.
	 * 
	 * Be sure to set the ID of the view in this method, especially if you're
	 * using a ListActivity or ListFragment.
	 * 
	 * @param context
	 * @param attrs
	 *            AttributeSet from wrapped class. Means that anything you
	 *            include in the XML layout declaration will be routed to the
	 *            created View
	 * @return New instance of the Refreshable View
	 */
	protected abstract T createRefreshableView(Context context,
			AttributeSet attrs);

	protected final int getCurrentMode() {
		return currentMode;
	}

	protected final LoadingLayout getFooterLayout() {
		return footerLayout;
	}

	protected final LoadingLayout getHeaderLayout() {
		return headerLayout;
	}

	protected final int getHeaderHeight() {
		return headerHeight;
	}

	protected final int getMode() {
		return mode;
	}

	/**
	 * Implemented by derived class to return whether the View is in a state
	 * where the user can Pull to Refresh by scrolling down.
	 * 
	 * @return true if the View is currently the correct state (for example, top
	 *         of a ListView)
	 */
	protected abstract boolean isReadyForPullDown();

	/**
	 * Implemented by derived class to return whether the View is in a state
	 * where the user can Pull to Refresh by scrolling up.
	 * 
	 * @return true if the View is currently in the correct state (for example,
	 *         bottom of a ListView)
	 */
	protected abstract boolean isReadyForPullUp();

	private void init(Context context, AttributeSet attrs) {
		MAX_MOVE_HEIGHT = (int) context.getResources().getDimension(R.dimen.most_move_distance);
		HEAD_HEIGHT = (int) context.getResources().getDimension(R.dimen.head_height);// 为了适应下拉刷新背景图，所以设置的大一些
		DELTA_Y = HEAD_HEIGHT - MAX_MOVE_HEIGHT;
		MAX_LENGHT = MAX_MOVE_HEIGHT;

		mDetector = new GestureDetector(this);
		mDetector.setIsLongpressEnabled(false);
		mFlinger = new FlingRunnable();

		MAX_LENGHT = MAX_MOVE_HEIGHT;
		setDrawingCacheEnabled(false);
		setBackgroundDrawable(null);
		setClipChildren(false);
		mDetector.setIsLongpressEnabled(false);
		mPading = -HEAD_HEIGHT;
		mLastTop = -HEAD_HEIGHT;

		// Styleables from XML
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.PullToRefresh);
		if (a.hasValue(R.styleable.PullToRefresh_mode)) {
			mode = a.getInteger(R.styleable.PullToRefresh_mode,
					MODE_PULL_DOWN_TO_REFRESH);
		}

		// Refreshable View
		// By passing the attrs, we can add ListView/GridView params via XML
		refreshableView = this.createRefreshableView(context, attrs);
		this.addRefreshableView(context, refreshableView);

		// Loading View Strings
		String pullLabel = context
				.getString(R.string.pull_to_refresh_pull_label);
		String refreshingLabel = context
				.getString(R.string.pull_to_refresh_refreshing_label);
		String releaseLabel = context
				.getString(R.string.pull_to_refresh_release_label);

		// Add Loading Views
		if (mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH) {
			headerLayout = new LoadingLayout(context,
					MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel,
					refreshingLabel);
			addView(headerLayout, 0, new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, HEAD_HEIGHT));// 确定的高度，用来适应topbar
			// addView(headerLayout, 0, new
			// LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
			// ViewGroup.LayoutParams.WRAP_CONTENT));
			measureView(headerLayout);
			headerHeight = headerLayout.getMeasuredHeight();
		}

		// Styleables from XML
		if (a.hasValue(R.styleable.PullToRefresh_headerTextColor)) {
			final int color = a.getColor(
					R.styleable.PullToRefresh_headerTextColor, Color.BLACK);
			if (null != headerLayout) {
				// headerLayout.setTextColor(color);
				headerLayout.setTextColor(Color.rgb(98, 98, 98));
			}
			if (null != footerLayout) {
				footerLayout.setTextColor(color);
			}
		}
		if (a.hasValue(R.styleable.PullToRefresh_headerBackground)) {
			this.setBackgroundResource(a.getResourceId(
					R.styleable.PullToRefresh_headerBackground, Color.WHITE));
		}
		if (a.hasValue(R.styleable.PullToRefresh_adapterViewBackground)) {
			refreshableView.setBackgroundResource(a.getResourceId(
					R.styleable.PullToRefresh_adapterViewBackground,
					Color.WHITE));
		}
		a.recycle();

		if (mode != MODE_BOTH) {
			currentMode = mode;
		}

		bottomView = getChildAt(1);

	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	private boolean isReadyForPull() {
		switch (mode) {
		case MODE_PULL_DOWN_TO_REFRESH:
			return isReadyForPullDown();
		case MODE_PULL_UP_TO_REFRESH:
			return isReadyForPullUp();
		case MODE_BOTH:
			return isReadyForPullUp() || isReadyForPullDown();
		}
		return false;
	}

	public static interface OnRefreshListener {

		public void onRefresh();

	}

	public static interface OnLastItemVisibleListener {

		public void onLastItemVisible();

	}

	public static interface OnSpaceStateChangeListener {

		public void onStart();

		public void onStop();

	}

	@Override
	public void setLongClickable(boolean longClickable) {
		getRefreshableView().setLongClickable(longClickable);
	}

	// ==============================================
	// 瀑布流顶部header状态改变
	// ==============================================

	private OnMoniterHeaderStateChangeListener onMoniterHeaderChangelistener;

	/**
	 * 用来模拟下拉刷新的view改变状态回调
	 * 
	 * @ClassName: OnMoniterHeaderStateChangeListener
	 * @author 王玉洁
	 * @mail wangyujie@youku.com
	 * @date 2013-7-17 上午11:48:58
	 */
	public interface OnMoniterHeaderStateChangeListener {
		/**
		 * 头部view可见
		 * 
		 * @Title: onHeaderShow
		 * @return void
		 * @date 2013-7-17 上午11:49:30
		 */
		public void onHeaderShow();

		/**
		 * 头部view隐藏
		 * 
		 * @Title: onHeaderHide
		 * @return void
		 * @date 2013-7-17 上午11:49:43
		 */
		public void onHeaderHide();

		/**
		 * 显示下拉可以刷新
		 * 
		 * @Title: OnPullToRefresh
		 * @return void
		 * @date 2013-7-17 上午11:49:55
		 */
		public void OnPullToRefresh();

		/**
		 * 显示松开可以刷新
		 * 
		 * @Title: onReleaseToRefresh
		 * @return void
		 * @date 2013-7-17 上午11:50:08
		 */
		public void onReleaseToRefresh();

		/**
		 * 显示刷新中
		 * 
		 * @Title: onRefreshing
		 * @return void
		 * @date 2013-7-17 上午11:50:26
		 */
		public void onRefreshing();

		/**
		 * 重置布局
		 * 
		 * @Title: onRest
		 * @return void
		 * @date 2013-7-17 上午11:50:35
		 */
		public void onRest();

	}

	public final void setOnMoniterHeaderStateChangeListener(
			OnMoniterHeaderStateChangeListener listener) {
		onMoniterHeaderChangelistener = listener;
		if (onMoniterHeaderChangelistener != null) {
			onMoniterHeaderChangelistener.onHeaderHide();
		}
	}

	public final void setMoniterHeaderView(
			LoadingLayout mMoniterHeaderLoadingLayout) {
		this.mMoniterHeaderLoadingLayout = mMoniterHeaderLoadingLayout;
	}

	// 如果listview或scrollview显示header，则下拉刷新的header则不显示
	public void setHeaderInVisible(boolean invisible) {
		headerLayout.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
	}

	// 如果listview或scrollview显示header，则下拉刷新的header则不显示(仅仅是字符不显示，背景图还需要显示)
	public void setHeaderStateVisible(boolean invisible) {
		headerLayout.setTextPartVisVisible(invisible);
	}

	/************************************ 实现部分 ****************************************************************/

	public static final int STATE_REFRESH = 1;
	public static final int SCROLL_TO_CLOSE = 2;
	public static final int SCROLL_TO_REFRESH = 3;
	public double SCALE = 0.5d;
	private static final int CLOSEDELAY = 300;
	private static final int REFRESHDELAY = 300;
	private int mState;
	private GestureDetector mDetector;
	private FlingRunnable mFlinger;
	private int mPading;
	private int mDestPading;// 基准线
	private int mLastTop;
	private boolean listviewDoScroll = false;
	private boolean isFirstLoading = false;
	private boolean mLongPressing;// 如果设置为true说明刚好到了执行长按的时间
	private boolean mPendingRemoved = false;//
	private MotionEvent downEvent;
	private CheckForLongPress mPendingCheckForLongPress = new CheckForLongPress();
	private CheckForLongPress2 mPendingCheckForLongPress2 = new CheckForLongPress2();
	private float lastY;

	private View bottomView;

	public void setTouchScale(double scale) {
		SCALE = scale;
	}

	/**
	 * 长按检查方法执行1线程
	 * 
	 * @author Administrator
	 * 
	 */
	private class CheckForLongPress implements Runnable {
		public void run() {
			if (refreshableView instanceof AbsListView) {
				if (((AbsListView) refreshableView)
						.getOnItemLongClickListener() == null) {
				} else {
					postDelayed(mPendingCheckForLongPress2, 100);
				}
			}
		}
	}

	/**
	 * 长按检查方法执行2线程 ----> 延后 100
	 * 
	 * @author Administrator
	 * 
	 */
	private class CheckForLongPress2 implements Runnable {
		public void run() {
			mLongPressing = true;
			MotionEvent e = MotionEvent.obtain(
					downEvent.getDownTime(),
					downEvent.getEventTime()
							+ ViewConfiguration.getLongPressTimeout(),
					MotionEvent.ACTION_CANCEL, downEvent.getX(),
					downEvent.getY(), downEvent.getMetaState());
			PullToRefreshBase.super.dispatchTouchEvent(e);
		}
	}

	class FlingRunnable implements Runnable {

		public void startCommon() {
			removeCallbacks(this);
		}

		public void run() {
			boolean noFinish = mScroller.computeScrollOffset();
			int curY = mScroller.getCurrY();
			int deltaY = curY - mLastFlingY;
			if (noFinish) {
				if (onSpaceStateChangeListener != null) {
					onSpaceStateChangeListener.onStart();
				}
				move(deltaY, true);
				mLastFlingY = curY;
				post(this);
			} else {
				time = -1L;// 动画结束，需要把显示时间归位，为下次展示做准备
				if (onSpaceStateChangeListener != null) {
					onSpaceStateChangeListener.onStop();
				}
				removeCallbacks(this);
				if (mState == SCROLL_TO_CLOSE) {
					mState = -1;
				}
			}
		}

		public void startUsingDistance(int distance, int duration) {
			if (distance == 0)
				distance--;
			startCommon();
			mLastFlingY = 0;
			mScroller.startScroll(0, 0, 0, distance, duration);
			post(this);
			if (onSpaceStateChangeListener != null) {
				onSpaceStateChangeListener.onStart();
			}
		}

		private int mLastFlingY;
		private Scroller mScroller;

		public FlingRunnable() {
			mScroller = new Scroller(getContext());
		}
	}

	/** deltaY > 0 向上 */
	private boolean move(float deltaY, boolean auto) {
		// move 方法执行 "
		if (deltaY > 0 && headerLayout.getTop() == -HEAD_HEIGHT) {
			mPading = -HEAD_HEIGHT;
			return false;
		}

		if (auto) {
			// move 方法执行
			if (headerLayout.getTop() - deltaY < mDestPading) {
				deltaY = headerLayout.getTop() - mDestPading;
			}
			headerLayout.offsetTopAndBottom((int) -deltaY);
			bottomView.offsetTopAndBottom((int) -deltaY);
			mPading = headerLayout.getTop();
			if (mDestPading == 0 - DELTA_Y
					&& headerLayout.getTop() == 0 - DELTA_Y
					&& mState == SCROLL_TO_REFRESH) {
				// onRefresh 刷新方法执行
				onRefresh();
				if (onSpaceStateChangeListener != null) {
					onSpaceStateChangeListener.onStop();
				}
			} else if (mDestPading == -HEAD_HEIGHT) {
			}
			invalidate();
			updateView();
			return true;
		} else {
			if (mState != STATE_REFRESH
					|| (mState == STATE_REFRESH && deltaY > 0)) {
				headerLayout.offsetTopAndBottom((int) -deltaY);
				bottomView.offsetTopAndBottom((int) -deltaY);
				mPading = headerLayout.getTop();
				changeSpaceTop(mPading);
			} else if (mState == STATE_REFRESH && deltaY < 0
					&& headerLayout.getTop() <= 0 - DELTA_Y) {
				if (headerLayout.getTop() - deltaY > 0 - DELTA_Y) {
					deltaY = headerLayout.getTop() + DELTA_Y;
				}
				headerLayout.offsetTopAndBottom((int) -deltaY);
				bottomView.offsetTopAndBottom((int) -deltaY);
				mPading = headerLayout.getTop();
				changeSpaceTop(mPading);
			}
		}
		if (deltaY > 0 && headerLayout.getTop() <= -HEAD_HEIGHT) {
			mPading = -HEAD_HEIGHT;
			deltaY = -HEAD_HEIGHT - headerLayout.getTop();
			headerLayout.offsetTopAndBottom((int) deltaY);
			bottomView.offsetTopAndBottom((int) deltaY);
			mPading = headerLayout.getTop();
			changeSpaceTop(mPading);
			updateView();
			invalidate();
			return false;
		}
		updateView();
		invalidate();
		return true;
	}

	private void updateView() {
		if (mState != STATE_REFRESH) {
			if (headerLayout.getTop() < 0 - DELTA_Y) {

				if (mLastTop >= 0 - DELTA_Y && mState != SCROLL_TO_CLOSE) {
					headerLayout.pullToRefresh();
					if (onMoniterHeaderChangelistener != null) {
						onMoniterHeaderChangelistener.OnPullToRefresh();
					}
				}

			} else if (headerLayout.getTop() > 0 - DELTA_Y) {

				if (mLastTop <= 0 - DELTA_Y) {
					headerLayout.releaseToRefresh();
					if (onMoniterHeaderChangelistener != null) {
						onMoniterHeaderChangelistener.onReleaseToRefresh();
					}
				}
			}
		}
		mLastTop = headerLayout.getTop();
	}

	private boolean release() {
		if (listviewDoScroll) {
			listviewDoScroll = false;
			return true;
		}
		if (headerLayout.getTop() > 0 - DELTA_Y) {
			scrollToUpdate(false);
		} else {
			scrollToClose();
		}
		invalidate();
		return false;
	}

	private void scrollToClose() {
		mDestPading = -HEAD_HEIGHT;
		mFlinger.startUsingDistance(MAX_LENGHT, CLOSEDELAY);
		if (onMoniterHeaderChangelistener != null) {
			onMoniterHeaderChangelistener.onHeaderHide();
		}
	}

	private void scrollToUpdate(boolean load) {
		mState = SCROLL_TO_REFRESH;

		mDestPading = 0 - DELTA_Y;
		if (load) {
			mFlinger.startUsingDistance(50, REFRESHDELAY);
			load = false;
		} else
			mFlinger.startUsingDistance(headerLayout.getTop() + DELTA_Y,
					REFRESHDELAY);
	}

	private void scrollToUpdate_refreshStart() {
		mDestPading = -HEAD_HEIGHT;
		mFlinger.startUsingDistance(-MAX_LENGHT, REFRESHDELAY);
	}

	private void onRefresh() {

		mState = STATE_REFRESH;
		headerLayout.refreshing();
		if (onMoniterHeaderChangelistener != null) {
			onMoniterHeaderChangelistener.onRefreshing();
		}

		if (onRefreshListener != null) {
			onRefreshListener.onRefresh();
		}
	}

	public void onRefreshComplete() {
		this.postDelayed(new Runnable() {
			@Override
			public void run() {
				onRefreshComplete(null);
			}
		}, 100);
	}

	private void onRefreshComplete(String date) {
		mState = SCROLL_TO_CLOSE;
		headerLayout.reset();
		if (onMoniterHeaderChangelistener != null) {
			onMoniterHeaderChangelistener.onRest();
		}
		scrollToClose();
	}

	public void onRefreshStart() {
		if (mState != STATE_REFRESH) {

			if (headerLayout.getTop() > -HEAD_HEIGHT) {
				mFlinger.startCommon();
				mPading = -HEAD_HEIGHT;
				invalidate();
			}
			this.post(new Runnable() {

				@Override
				public void run() {
					if (refreshableView instanceof AbsListView) {
						((AbsListView) refreshableView).setSelection(0);
					} else if (refreshableView instanceof ScrollView) {
						((ScrollView) refreshableView).smoothScrollTo(0, 0);
					}

					mState = STATE_REFRESH;
					headerLayout.refreshing();
					if (onMoniterHeaderChangelistener != null) {
						onMoniterHeaderChangelistener.onRefreshing();
						onMoniterHeaderChangelistener.onHeaderShow();
					}

					// updataTimeText();

					scrollToUpdate_refreshStart();
				}
			});
		}
	}

	public boolean dispatchTouchEvent(MotionEvent e) {

		if (!isPullToRefreshEnabled) {
			return super.dispatchTouchEvent(e);
		}
		if (isFirstLoading) {
			return false;
		}
		int action;
		float y = e.getY();
		action = e.getAction();
		if (mLongPressing && action != MotionEvent.ACTION_DOWN) {
			return false;
		}
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			mLongPressing = true;
		}
		boolean handled = true;
		handled = mDetector.onTouchEvent(e);
		switch (action) {
		case MotionEvent.ACTION_UP:
			boolean f1 = bottomView.getTop() <= e.getY()
					&& e.getY() <= bottomView.getBottom();
			if (!handled && headerLayout.getTop() == -HEAD_HEIGHT && f1
					|| mState == STATE_REFRESH) {
				super.dispatchTouchEvent(e);
			} else {
				// 执行释放方法
				handled = release();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			handled = release();
			super.dispatchTouchEvent(e);
			break;
		case MotionEvent.ACTION_DOWN:
			downEvent = e;
			mLongPressing = false;
			// 长按的时间间隔
			postDelayed(mPendingCheckForLongPress,
					ViewConfiguration.getLongPressTimeout() + 100);
			mPendingRemoved = false;
			super.dispatchTouchEvent(e);
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaY = lastY - y;
			lastY = y;
			if (!mPendingRemoved) {
				removeCallbacks(mPendingCheckForLongPress);
				mPendingRemoved = true;
			}

			if (!handled && headerLayout.getTop() == -HEAD_HEIGHT) {
				try {
					return super.dispatchTouchEvent(e);
				} catch (Exception e2) {
					e2.printStackTrace();
					return true;
				}
			} else if (handled && bottomView.getTop() > 0 && deltaY < 0) {// deltaY小于0，向�?
				e.setAction(MotionEvent.ACTION_CANCEL);
				super.dispatchTouchEvent(e);
			}
			break;

		default:
			super.dispatchTouchEvent(e);
			break;
		}

		return true;
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public boolean onFling(MotionEvent motionevent, MotionEvent e, float f,
			float f1) {
		return false;
	}

	protected void onLayout(boolean flag, int i, int j, int k, int l) {
		int top = mPading;
		int w = getMeasuredWidth();
		if (headerLayout != null) {
			headerLayout.layout(0, top, w, top + HEAD_HEIGHT);
		}

		int h = getMeasuredHeight() + mPading + HEAD_HEIGHT;
		if (bottomView != null) {
			try {
				bottomView.layout(0, top + HEAD_HEIGHT, w, h);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				System.gc();
				return;
			}
		}
		changeSpaceTop(mPading);
	}

	public void onLongPress(MotionEvent e) {
	}

	/** deltaY > 0 向上 */
	public boolean onScroll(MotionEvent curdown, MotionEvent cur, float deltaX,
			float deltaY) {
		deltaY = (float) ((double) deltaY * SCALE);
		boolean handled = false;
		boolean flag = false;

		if (isReadyForPullDown()) {
			if (onHeadPullListener != null) {
				onHeadPullListener.onPull();
			}
			flag = true;
		}

		// 两个条件（需要改变布局）：
		// 1.向下滑动，并且下拉刷新已经准备好
		// 2.现在下拉刷新的布局不在最顶部，这时候无论向上向下有无手势点触都要改变布局
		if (deltaY < 0F && flag || getChildAt(0).getTop() > -HEAD_HEIGHT) { // deltaY
																			// <
																			// 0
																			// 向下,条件是或的关系
			handled = move(deltaY, false);

			// updataTimeText();

			if (onMoniterHeaderChangelistener != null) {
				onMoniterHeaderChangelistener.onHeaderShow();
			}
			if (onSpaceStateChangeListener != null) {
				onSpaceStateChangeListener.onStart();
			}
		} else {
			handled = false;
			if (onSpaceStateChangeListener != null) {
				onSpaceStateChangeListener.onStop();
			}
		}
		return handled;
	}

	public void onShowPress(MotionEvent motionevent) {
	}

	public boolean onSingleTapUp(MotionEvent motionevent) {
		return false;
	}

	private long lastBroadCastTime = 0;
	private int eventCache = 0;

	private boolean isShouldChangTop;// 是否需要发送广播改变顶部大小
	private long mySpaceflag = -1L;// 用来标示自己空间的标志

	public void setIsShouldChangTop(boolean isShouldChangTop) {
		this.isShouldChangTop = isShouldChangTop;
	}

	public void setMyspaceFlag(long flag) {
		mySpaceflag = flag;
	}

	protected void changeSpaceTop(int padding) {
		// if(onSpaceTopChangeListener != null){
		// onSpaceTopChangeListener.onSpaceTopChange(padding + HEADER_HEIGHT);
		// }

		if (isShouldChangTop && listener != null) {
			int pad = padding + HEAD_HEIGHT;
			if (Math.abs(pad) > 0) {
				// listener.changeSpaceTop(pad);
			}

		}
	}

	private ChangeSpaceTopListener listener;

	public void setChangeSpaceTopListener(ChangeSpaceTopListener listener) {
		this.listener = listener;
	}

	public interface ChangeSpaceTopListener {
		public void changeSpaceTop(int padding);
	}

	// /////////////////背景图部分//////////////////////

	public void setHeaderImgHeight(int height) {
		headerLayout.setBackGroundImgHeight(height);
	}

	public void setHeaderImg(Drawable drawable) {
		headerLayout.setBackGroundImg(drawable);
	}

	public OnHeadPullListener onHeadPullListener;

	public interface OnHeadPullListener {
		public void onPull();
	}

	public void setOnHeadPullListener(OnHeadPullListener onHeadPullListener) {
		this.onHeadPullListener = onHeadPullListener;
	}

	/*--------------------------显示更新时间----------------------------------*/

	/**
	 * 设置下拉刷新的url地址，用来获取更新时间
	 * 
	 * @Title: setCacheRequestUrl
	 * @param requestUrl
	 * @param isPrivateData
	 * @return void
	 * @date 2013-9-4 上午10:54:57
	 */
	public void setCacheRequestUrl(String requestUrl, boolean isPrivateData) {

		this.requestUrl = requestUrl;
		this.isPrivateData = isPrivateData;
		shouldShowUpdataTime = true;

	}

}
