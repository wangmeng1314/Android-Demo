package com.example.bluetooth;

import java.util.Timer;

import java.util.TimerTask;

import org.achartengine.GraphicalView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import com.example.service.ChartService;

/**
 * ����:����������ؽڵ��״̬,�������ݸ��û� **************��:�¶�,��ǰ�Ƿ���������ȵ�.
 * 
 * @author Henry
 */
public class MonitorPatternActivity extends ActionBarActivity {
	private LinearLayout right;
	private GraphicalView rightview;
	private ChartService rightservice;
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitorpattern);
		// ��ð�ť
		// ���ͼ����չʾ����
		right = (LinearLayout) findViewById(R.id.right_temperature_curve);
		// ��ʼ�����ұߵ�ͼ��
		rightservice = new ChartService(this);
		rightservice.setXYMultipleSeriesDataset("���¶�����");
		rightservice.setXYMultipleSeriesRenderer(100, 50, "�¶�", "ʱ��", "�����¶�����",
				Color.RED, Color.RED, Color.GREEN, Color.GREEN);
		rightview = rightservice.getGraphicalView();
		// ���ұߵ�ͼ�����ӵ�����������ȥ
		rightview.setFocusable(false);
		rightview.setBackgroundColor(Color.BLACK);
		rightview.setSelected(false);
		rightview.setClickable(false);
		rightview.setEnabled(false);
		right.addView(rightview, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// ��ʱ��
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendMessage(handler.obtainMessage());
			}
		}, 10, 100);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * ���½���
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// ��������
			int addY = (int) (Math.random() * 10 + 30);
			// new BluetoothData().sendData(addY);
			rightservice.updateChart(addY);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}
}