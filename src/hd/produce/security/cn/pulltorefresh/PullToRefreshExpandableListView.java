package hd.produce.security.cn.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ExpandableListView;

public class PullToRefreshExpandableListView extends
		PullToRefreshAdapterViewBase<ExpandableListView> {

	// private LoadingLayout headerLoadingView;
	// private LoadingLayout footerLoadingView;

	class InternalListView extends ExpandableListView implements
			EmptyViewMethodAccessor {

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshExpandableListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}
	}

	public PullToRefreshExpandableListView(Context context) {
		super(context);
		this.setDisableScrollingWhileRefreshing(false);
	}

	public PullToRefreshExpandableListView(Context context, int mode) {
		super(context, mode);
		this.setDisableScrollingWhileRefreshing(false);
	}

	public PullToRefreshExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDisableScrollingWhileRefreshing(false);
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((InternalListView) getRefreshableView()).getContextMenuInfo();
	}

	@Override
	protected final ExpandableListView createRefreshableView(Context context,
			AttributeSet attrs) {
		ExpandableListView lv = new InternalListView(context, attrs);

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		return lv;
	}

}
