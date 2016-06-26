package com.example.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.achartengine.GraphicalView;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.service.ChartService;
import com.example.service.CommonUtil;
import com.oracle.moudle.BluetoothData;

/**
 * 
 * @author Henry
 * @version 1.0
 * @since 2015/12/18
 * 
 */
public class MainActivity extends ActionBarActivity implements OnClickListener {
	// ����������Ҫ�õ��ĳ���
	private static final int REQUEST_ENABLE_BT = 0;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int MSG_NEW_DATA = 3;
	// ��������&&mac��ַ
	private BluetoothData bluData = new BluetoothData();
	private String macNode1 = "2b282b06004b1200";
	private String macNode2 = "92d76002004b1200";
	// �����豸
	private BluetoothAdapter mBluetoothAdapter;
	public ConnectedThread mConnectedThread;
	// ������ȡ�õĽ��
	private int result;
	// ����������Ҫ�õ���UUID
	private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	// ��ӡ�����Ϣ�ı�ʾ
	public static String TAG = "MAINACTIVITY";
	String s = new String();
	// tab��������Ҫ�õ��Ŀؼ�
	private ViewPager mViewPager;
	private PagerAdapter mAdapter;
	private List<View> mViews = new ArrayList<View>();
	// TAB
	private LinearLayout mTabWeixin;
	private LinearLayout mTabFrd;
	private LinearLayout mTabSetting;

	private ImageButton mWeixinImg;
	private ImageButton mFrdImg;
	private ImageButton mSettingImg;
	// view
	private View tab01;
	private View tab02;
	private View tab04;
	// �����豸�Ļ�����Ϣ
	private String name;
	private String address;
	private String connectResult = "����ʧ��";
	// ����һ�õ��Ŀؼ�,���մ��ϵ��µ�˳�����
	private TextView txtnNme;
	private TextView txtAddress;
	private TextView txtConnectMotion;
	private Button btnScan;

	// ������õ��Ŀؼ�
	private Button btnOpenClock;
	private Button btnCloseClock;
	private Button btnOpenLight;
	private Button btnCloseLight;
	private Button btnOpenExerise;
	private Button btnCloseExerise;
	// ����ͼ
	private LinearLayout right;
	private GraphicalView rightview;
	private GraphicalView leftview;
	private ChartService rightservice;
	private ChartService leftservice;
	private Timer timer;

	// ִ����Ӧ������İ�ťʵ��
	// �������õ��Ŀؼ�
	private TextView txtTempature;
	private TextView txtHavePerson;
	private TextView txtHaveRain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ������������ʽ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// ��ʼ��������
		initView();
		// ��ʼ���¼�����
		initEvents();
		// ��ʼ������ͼ
		initChart();
		// ��ȡ���ص�����������
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// �ж������豸�Ƿ��Ѿ�����
		if (mBluetoothAdapter == null) {
			Toast.makeText(MainActivity.this, "�����豸δ���û򱾵��������豸!",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
	}

	/**
	 * ��ʼ������ͼ
	 */
	private void initChart() {
		// �õ�view
		right = (LinearLayout) tab02.findViewById(R.id.id_linechart);
		// ��ʼ�����ұߵ�ͼ��
		rightservice = new ChartService(this);
		rightservice.setXYMultipleSeriesDataset("���¶�����");
		rightservice.setXYMultipleSeriesRenderer(100, 40, "�¶�", "ʱ��", "�����¶�����",
				Color.RED, Color.RED, Color.GREEN, Color.GREEN);
		rightview = rightservice.getGraphicalView();
		// ������ߵ�ͼ��
		// ���ұߵ�ͼ����ӵ�����������ȥ
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
		handler = new Handler() {

			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// ������ʪ�ȵ�
				// txtTempature.setText("");
				// txtTempature.setText("��ǰ�¶�Ϊ��" + bluData.getTemperature()
				// + "--��ǰʪ��Ϊ��" + bluData.getHumidity());
				// Toast.makeText(MainActivity.this, "��ǰ�¶�Ϊ��" +
				// bluData.getTemperature()
				// + "--��ǰʪ��Ϊ��" + bluData.getHumidity(),
				// Toast.LENGTH_SHORT).show();
				// send data
				int addY = bluData.getTemperature();
				rightservice.updateChart(addY);
			}
		};
	}

	/**
	 * ���½�����߳�
	 */
	private class updataUserInterface extends Thread {

		@Override
		public void run() {
			super.run();
			while (true) {
				uiHandler.sendMessage(uiHandler.obtainMessage());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * ���»�ͼ����,���½���
	 */
	private Handler handler;

	/**************************************** initView() ***********************************************************/
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		// tabs
		mTabWeixin = (LinearLayout) findViewById(R.id.id_tab_weixin);
		mTabFrd = (LinearLayout) findViewById(R.id.id_tab_frd);
		mTabSetting = (LinearLayout) findViewById(R.id.id_tab_settings);
		// ImageButton
		mWeixinImg = (ImageButton) findViewById(R.id.id_tab_weixin_img);
		mFrdImg = (ImageButton) findViewById(R.id.id_tab_frd_img);
		mSettingImg = (ImageButton) findViewById(R.id.id_tab_settings_img);

		LayoutInflater mInflater = LayoutInflater.from(this);
		tab01 = mInflater.inflate(R.layout.tab01, null);
		/**** **********�õ�����һ��4���ؼ� *****************************************/
		txtnNme = (TextView) tab01.findViewById(R.id.id_device_name);
		txtAddress = (TextView) tab01.findViewById(R.id.id_device_address);
		txtConnectMotion = (TextView) tab01
				.findViewById(R.id.id_device_connectmotion);
		tab02 = mInflater.inflate(R.layout.tab02, null);
		btnScan = (Button) tab01.findViewById(R.id.id_device_scan);
		/************** �õ�����6���Ŀؼ� *********************************************/
		btnOpenClock = (Button) tab02.findViewById(R.id.id_openClock);// ��������
		btnCloseClock = (Button) tab02.findViewById(R.id.id_closeClock);// �رձ���
		btnOpenLight = (Button) tab02.findViewById(R.id.id_openlight); // �����ƹ�
		btnCloseLight = (Button) tab02.findViewById(R.id.id_closeLight); // �����ƹ�
		btnOpenExerise = (Button) tab02.findViewById(R.id.id_openExerise);// ��������ģʽ
		btnCloseExerise = (Button) tab02.findViewById(R.id.id_closeExerise);// �ر�����
		tab04 = mInflater.inflate(R.layout.tab04, null);
		// �õ�����3�������ؼ�
		txtTempature = (TextView) tab04.findViewById(R.id.id_tempature);
		txtHavePerson = (TextView) tab04.findViewById(R.id.id_haveperson);
		txtHaveRain = (TextView) tab04.findViewById(R.id.id_haverain);
		// ���ñ���ͼƬ
		tab01.setBackgroundResource(R.drawable.ic_back_1);
		tab02.setBackgroundResource(R.drawable.ic_back_2);
		tab04.setBackgroundResource(R.drawable.ic_back_3);
		mViews.add(tab01);
		mViews.add(tab02);
		mViews.add(tab04);
		mAdapter = new PagerAdapter() {

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mViews.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = mViews.get(position);
				container.addView(view);
				return view;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return mViews.size();
			}
		};
		// ����������
		mViewPager.setAdapter(mAdapter);
	}

	/**************************************** initView(); ***********************************************************/
	/***************************************** initEvents(); *********************************************************/
	private void initEvents() {
		// ����������������ť�ļ���
		mTabWeixin.setOnClickListener(this);
		mTabFrd.setOnClickListener(this);
		mTabSetting.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int currentItem = mViewPager.getCurrentItem();
				resetImg();
				switch (currentItem) {
				case 0:
					mWeixinImg.setImageResource(R.drawable.tab_weixin_pressed);
					break;
				case 1:
					mFrdImg.setImageResource(R.drawable.tab_find_frd_pressed);
					break;
				case 2:
					mSettingImg
							.setImageResource(R.drawable.tab_settings_pressed);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		// ���ý���һ�ؼ��ļ����¼�
		btnScan.setOnClickListener(this);
		// ���ý�����ؼ��ļ����¼�
		btnOpenClock.setOnClickListener(this);
		btnCloseClock.setOnClickListener(this);
		btnOpenLight.setOnClickListener(this);
		btnCloseLight.setOnClickListener(this);
		btnOpenExerise.setOnClickListener(this);
		btnCloseExerise.setOnClickListener(this);
	}

	/***************************************** initEvents(); *********************************************************/
	@Override
	protected void onStart() {
		super.onStart();
		if (!mBluetoothAdapter.isEnabled()) {
			// ������������ͼ
			Intent bluIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// ���������������ǰû����������,�û�һ����������,��ֱ�ӽ��뵽��������������
			startActivityForResult(bluIntent, REQUEST_ENABLE_BT);
		}
	}

	/**
	 * ƥ�������������淵�ص���Ϣ,������Ӧ�Ĳ���
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ���ݷ��ص���Ϣ��������
		switch (requestCode) {
		// ƥ���û�����app����������Ϊ:
		case REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				// ����������������
				Intent serverIntent = new Intent(MainActivity.this,
						DeviceListActivity.class);
				// �������Ĳ�������ƥ��
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			} else {
				Log.d(TAG, "bluetooth not enabled!");
				finish();
				return;
			}
			break;
		case REQUEST_CONNECT_DEVICE:
			if (resultCode != Activity.RESULT_OK) {
				return;
			} else {
				String info = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// �����豸����
				name = info.substring(0, info.length() - 17);
				System.out.println(name);
				// ����MAC��ַ��������豸
				address = info.substring(info.length() - 17);
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				connect(device);
			}
			break;
		default:
			// ��ƥ����Ϣ
			break;
		}
	}

	/**
	 * ����MAC��ַ�����豸
	 * 
	 * @param device
	 *            ��������MAC��ַ�õ���Ӳ���豸
	 */
	private void connect(BluetoothDevice device) {
		Log.d(TAG, "connect to " + device);
		// ��ʱ�������⿪��һ���߳�
		new ConnectThread(device).start();
	}

	/**
	 * �ڲ���,�������ӵ��߳�
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;
			// �õ�����socket����
			try {
				tmp = device.createRfcommSocketToServiceRecord(UUID
						.fromString(SPP_UUID));
			} catch (IOException e) {
				Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;
		}

		@Override
		public void run() {
			super.run();
			// ֹͣ����
			mBluetoothAdapter.cancelDiscovery();
			try {
				mmSocket.connect();
				connectResult = "���ӳɹ�";
				/****************** ʹ��Handler����ҳ��״̬ **************************************/
				myHandler.sendMessage(myHandler.obtainMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Log.d(TAG, "�����豸����ʧ��!");
			}
			// ��ʼ�������������豸������
			mConnectedThread = new ConnectedThread(mmSocket);
			mConnectedThread.start();
		}
	}

	/**
	 * This thread runs during a connection with a remote device. It handles all
	 * incoming and outgoing transmissions.
	 */
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// �õ��������������
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			@SuppressWarnings("unused")
			int bytesLength = 0;
			StringBuffer res = new StringBuffer();
			byte[] buffer = new byte[1];
			while (true) {
				try {
					for (int i = 0; i < 14; i++) {
						mmInStream.read(buffer);
						res.append(CommonUtil.bytesToHexString(buffer));
					}
					// System.out.println("����Ϊ:" + res.toString());
					String macAdress = res.substring(0, 16);
					String result = res.substring(16);
					// �ж����ĸ��ڵ�����ݣ���Ӧ�ڵ���ж�Ӧ�Ľ���
					if (macNode1.equalsIgnoreCase(macAdress)) {
						bluData.setTemperature(CommonUtil.hexToTen(result
								.substring(8, 10)));
						bluData.setHumidity(CommonUtil.hexToTen(result
								.substring(4, 6)));
						// �������������ڵ㶼Ҫ���µ�
						bluData.setHavePerson(result.substring(2, 4) + "-"
								+ macAdress);
					} else if (macNode2.equalsIgnoreCase(macAdress)) {
						bluData.setHaveRain(result.substring(10, 12));
						bluData.setHaveSmoke(result.substring(6, 8));
						// �������������ڵ㶼Ҫ�и��µ�
						bluData.setHavePerson(result.substring(2, 4) + "-"
								+ macAdress);
					}
					// System.out.println(result+"-----"+macAdress);
					System.out.println(bluData.toString());
					Thread.sleep(100);
					// ��������
					res = new StringBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * ��������
		 * 
		 * @throws InterruptedException
		 */
		public void writeChar(String message) throws InterruptedException {
			try {
				mmOutStream.write(message.getBytes());
				Thread.sleep(100);
			} catch (IOException e) {
				Log.e(TAG, "Exception during write");
			}
		}

		/**
		 * �ر�socket����
		 */
		public void cancel() {
			try {
				if (mmSocket != null) {
					mmSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �ͷ�ռ�õ���Դ
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		resetImg();
		switch (v.getId()) {
		case R.id.id_tab_weixin:
			mViewPager.setCurrentItem(0);
			mWeixinImg.setImageResource(R.drawable.tab_weixin_pressed);
			break;
		case R.id.id_tab_frd:
			mViewPager.setCurrentItem(1);
			mFrdImg.setImageResource(R.drawable.tab_find_frd_pressed);
			break;
		case R.id.id_tab_settings:
			mViewPager.setCurrentItem(3);
			mSettingImg.setImageResource(R.drawable.tab_settings_pressed);
			new updataUserInterface().start();
			break;
		case R.id.id_device_scan:
			// ��������ɨ�����
			Intent serverIntent = new Intent(MainActivity.this,
					DeviceListActivity.class);
			// �������Ĳ�������ƥ��
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			break;
		case R.id.id_openClock:
			// ��������
			String message = "alarmon";
			try {
				mConnectedThread.writeChar(message);
				mConnectedThread.writeChar(message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_closeClock:
			// �رձ���
			String alarmoff = "alarmoff";
			try {
				mConnectedThread.writeChar(alarmoff);
				mConnectedThread.writeChar(alarmoff);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_openlight:
			// ����
			String openClock = "on";
			try {
				mConnectedThread.writeChar(openClock);
				mConnectedThread.writeChar(openClock);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_closeLight:
			// �رյƹ�
			String closeClock = "off";
			try {
				mConnectedThread.writeChar(closeClock);
				mConnectedThread.writeChar(closeClock);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_openExerise:
			// ���ֿ���
			String exerciseon = "exerciseon";
			try {
				mConnectedThread.writeChar(exerciseon);
				mConnectedThread.writeChar(exerciseon);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			break;
		case R.id.id_closeExerise:
			// ���ֹر�
			String exerciseoff = "exerciseoff";
			try {
				mConnectedThread.writeChar(exerciseoff);
				mConnectedThread.writeChar(exerciseoff);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}

	/**
	 * �����е�ͼƬ�л�Ϊ��ɫ��
	 */
	private void resetImg() {
		mWeixinImg.setImageResource(R.drawable.tab_weixin_normal);
		mFrdImg.setImageResource(R.drawable.tab_find_frd_normal);
		mSettingImg.setImageResource(R.drawable.tab_settings_normal);
	}

	/**
	 * �����豸������Ϣ
	 */
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			/*********************************** ����UI ************************************************/
			txtnNme.setText(name);
			txtAddress.setText(address);
			txtConnectMotion.setText(connectResult);
		}
	};
	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			txtTempature.setText("");
			// ��������
			Map<String, String> map = CommonUtil.disposeData(
					bluData.getHavePerson(), bluData.getHaveRain(),
					bluData.getHaveSmoke(), bluData.getTemperature(),
					bluData.getHumidity());
			txtTempature.setText(map.get("tem"));
			txtHavePerson.setText(map.get("person"));
			txtHaveRain.setText(map.get("smoke"));
			// System.out.println(res);
		}
	};
}