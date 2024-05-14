package hd.produce.security.cn;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Administrator
 *         待接收任务界面
 */
public class ReceivingTaskActivity extends MonitoredActivity {
    private Button back;
    private TextView title;
    private TextView done;

    private EditText taskName;
    private EditText republic;
    private EditText time;
    private EditText sampleClass;
    private EditText sampleArea;
    private EditText sampleLink;
    private EditText sender;
    private EditText sampleNum;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_to_be_received_task_layout);
        HdCnApp.getApplication().addActivity(this);
        initViews();
        setActions();
    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title_text);
        title.setText(R.string.receiving_task);

        taskName = (EditText) findViewById(R.id.task_name_edit);
        republic = (EditText) findViewById(R.id.republic_edit);
        time = (EditText) findViewById(R.id.date_end_edit);
        sampleClass = (EditText) findViewById(R.id.sample_type_edit);
        sampleArea = (EditText) findViewById(R.id.sample_area_edit);
        sampleLink = (EditText) findViewById(R.id.sample_link_edit);
        sender = (EditText) findViewById(R.id.sender_edit);
        sampleNum = (EditText) findViewById(R.id.sample_num_edit);
        done = (TextView) findViewById(R.id.done);
    }

    private void setActions() {
        back.setOnClickListener(onClickListener);
        done.setOnClickListener(onClickListener);
    }

    private void onBackClick() {
        super.onBackPressed();
    }

    private void onDoneClick() {

    }

    final OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.back:
                    onBackClick();
                    break;
                case R.id.done:
                    onDoneClick();
                    break;

                default:
                    break;
            }
        }
    };
}
