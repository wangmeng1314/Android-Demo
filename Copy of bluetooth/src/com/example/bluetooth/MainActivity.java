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
	// 蓝牙连接需要用到的常量
	private static final int REQUEST_ENABLE_BT = 0;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int MSG_NEW_DATA = 3;
	// 蓝牙数据&&mac地址
	private BluetoothData bluData = new BluetoothData();
	private String macNode1 = "2b282b06004b1200";
	private String macNode2 = "92d76002004b1200";
	// 蓝牙设备
	private BluetoothAdapter mBluetoothAdapter;
	public ConnectedThread mConnectedThread;
	// 蓝牙所取得的结果
	private int result;
	// 蓝牙连接需要用到的UUID
	private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	// 打印输出信息的标示
	public static String TAG = "MAINACTIVITY";
	String s = new String();
	// tab界面上需要用到的控件
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
	// 蓝牙设备的基本信息
	private String name;
	private String address;
	private String connectResult = "连接失败";
	// 界面一用到的控件,按照从上到下的顺序进行
	private TextView txtnNme;
	private TextView txtAddress;
	private TextView txtConnectMotion;
	private Button btnScan;

	// 界面二用到的控件
	private Button btnOpenClock;
	private Button btnCloseClock;
	private Button btnOpenLight;
	private Button btnCloseLight;
	private Button btnOpenExerise;
	private Button btnCloseExerise;
	// 折线图
	private LinearLayout right;
	private GraphicalView rightview;
	private GraphicalView leftview;
	private ChartService rightservice;
	private ChartService leftservice;
	private Timer timer;

	// 执行响应的命令的按钮实例
	// 界面三用到的控件
	private TextView txtTempature;
	private TextView txtHavePerson;
	private TextView txtHaveRain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置主界面样式
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 初始化主界面
		initView();
		// 初始化事件处理
		initEvents();
		// 初始化折线图
		initChart();
		// 获取本地的蓝牙适配器
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// 判断蓝牙设备是否已经启用
		if (mBluetoothAdapter == null) {
			Toast.makeText(MainActivity.this, "蓝牙设备未启用或本地无蓝牙设备!",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
	}

	/**
	 * 初始化折线图
	 */
	private void initChart() {
		// 得到view
		right = (LinearLayout) tab02.findViewById(R.id.id_linechart);
		// 开始设置右边的图表
		rightservice = new ChartService(this);
		rightservice.setXYMultipleSeriesDataset("右温度曲线");
		rightservice.setXYMultipleSeriesRenderer(100, 40, "温度", "时间", "环境温度曲线",
				Color.RED, Color.RED, Color.GREEN, Color.GREEN);
		rightview = rightservice.getGraphicalView();
		// 设置左边的图表
		// 将右边的图表添加到布局容器中去
		rightview.setFocusable(false);
		rightview.setBackgroundColor(Color.BLACK);
		rightview.setSelected(false);
		rightview.setClickable(false);
		rightview.setEnabled(false);
		right.addView(rightview, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// 定时器
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
				// 更新温湿度等
				// txtTempature.setText("");
				// txtTempature.setText("当前温度为：" + bluData.getTemperature()
				// + "--当前湿度为：" + bluData.getHumidity());
				// Toast.makeText(MainActivity.this, "当前温度为：" +
				// bluData.getTemperature()
				// + "--当前湿度为：" + bluData.getHumidity(),
				// Toast.LENGTH_SHORT).show();
				// send data
				int addY = bluData.getTemperature();
				rightservice.updateChart(addY);
			}
		};
	}

	/**
	 * 更新界面的线程
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
	 * 更新绘图界面,更新界面
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
		/**** **********得到界面一的4个控件 *****************************************/
		txtnNme = (TextView) tab01.findViewById(R.id.id_device_name);
		txtAddress = (TextView) tab01.findViewById(R.id.id_device_address);
		txtConnectMotion = (TextView) tab01
				.findViewById(R.id.id_device_connectmotion);
		tab02 = mInflater.inflate(R.layout.tab02, null);
		btnScan = (Button) tab01.findViewById(R.id.id_device_scan);
		/************** 得到界面6个的控件 *********************************************/
		btnOpenClock = (Button) tab02.findViewById(R.id.id_openClock);// 开启报警
		btnCloseClock = (Button) tab02.findViewById(R.id.id_closeClock);// 关闭报警
		btnOpenLight = (Button) tab02.findViewById(R.id.id_openlight); // 开启灯光
		btnCloseLight = (Button) tab02.findViewById(R.id.id_closeLight); // 开启灯光
		btnOpenExerise = (Button) tab02.findViewById(R.id.id_openExerise);// 开启娱乐模式
		btnCloseExerise = (Button) tab02.findViewById(R.id.id_closeExerise);// 关闭娱乐
		tab04 = mInflater.inflate(R.layout.tab04, null);
		// 得到界面3的三个控件
		txtTempature = (TextView) tab04.findViewById(R.id.id_tempature);
		txtHavePerson = (TextView) tab04.findViewById(R.id.id_haveperson);
		txtHaveRain = (TextView) tab04.findViewById(R.id.id_haverain);
		// 设置背景图片
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
		// 设置适配器
		mViewPager.setAdapter(mAdapter);
	}

	/**************************************** initView(); ***********************************************************/
	/***************************************** initEvents(); *********************************************************/
	private void initEvents() {
		// 设置主界面三个按钮的监听
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
		// 设置界面一控件的监听事件
		btnScan.setOnClickListener(this);
		// 设置界面二控件的监听事件
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
			// 启动蓝牙的意图
			Intent bluIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// 如果进入启动界面前没有启动蓝牙,用户一旦启动蓝牙,会直接进入到蓝牙的搜索界面
			startActivityForResult(bluIntent, REQUEST_ENABLE_BT);
		}
	}

	/**
	 * 匹配蓝牙搜索界面返回的信息,进行相应的操作
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 根据返回的信息处理请求
		switch (requestCode) {
		// 匹配用户进入app打开蓝牙的行为:
		case REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				// 启动蓝牙搜索界面
				Intent serverIntent = new Intent(MainActivity.this,
						DeviceListActivity.class);
				// 方便后面的参数进行匹配
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
				// 蓝牙设备名称
				name = info.substring(0, info.length() - 17);
				System.out.println(name);
				// 根据MAC地址获得蓝牙设备
				address = info.substring(info.length() - 17);
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				connect(device);
			}
			break;
		default:
			// 无匹配信息
			break;
		}
	}

	/**
	 * 根据MAC地址连接设备
	 * 
	 * @param device
	 *            根据蓝牙MAC地址得到的硬件设备
	 */
	private void connect(BluetoothDevice device) {
		Log.d(TAG, "connect to " + device);
		// 耗时操作另外开启一个线程
		new ConnectThread(device).start();
	}

	/**
	 * 内部类,负责连接的线程
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;
			// 得到蓝牙socket连接
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
			// 停止搜索
			mBluetoothAdapter.cancelDiscovery();
			try {
				mmSocket.connect();
				connectResult = "连接成功";
				/****************** 使用Handler更新页面状态 **************************************/
				myHandler.sendMessage(myHandler.obtainMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Log.d(TAG, "蓝牙设备连接失败!");
			}
			// 开始监听来自蓝牙设备的数据
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

			// 得到输入流和输出流
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
					// System.out.println("数据为:" + res.toString());
					String macAdress = res.substring(0, 16);
					String result = res.substring(16);
					// 判断是哪个节点的数据，对应节点进行对应的解析
					if (macNode1.equalsIgnoreCase(macAdress)) {
						bluData.setTemperature(CommonUtil.hexToTen(result
								.substring(8, 10)));
						bluData.setHumidity(CommonUtil.hexToTen(result
								.substring(4, 6)));
						// 有无人是两个节点都要更新的
						bluData.setHavePerson(result.substring(2, 4) + "-"
								+ macAdress);
					} else if (macNode2.equalsIgnoreCase(macAdress)) {
						bluData.setHaveRain(result.substring(10, 12));
						bluData.setHaveSmoke(result.substring(6, 8));
						// 有无人是两个节点都要有更新的
						bluData.setHavePerson(result.substring(2, 4) + "-"
								+ macAdress);
					}
					// System.out.println(result+"-----"+macAdress);
					System.out.println(bluData.toString());
					Thread.sleep(100);
					// 性能问题
					res = new StringBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 发送数据
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
		 * 关闭socket连接
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
	 * 释放占用的资源
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
			// 重新启动扫描界面
			Intent serverIntent = new Intent(MainActivity.this,
					DeviceListActivity.class);
			// 方便后面的参数进行匹配
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			break;
		case R.id.id_openClock:
			// 开启报警
			String message = "alarmon";
			try {
				mConnectedThread.writeChar(message);
				mConnectedThread.writeChar(message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_closeClock:
			// 关闭报警
			String alarmoff = "alarmoff";
			try {
				mConnectedThread.writeChar(alarmoff);
				mConnectedThread.writeChar(alarmoff);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_openlight:
			// 开灯
			String openClock = "on";
			try {
				mConnectedThread.writeChar(openClock);
				mConnectedThread.writeChar(openClock);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_closeLight:
			// 关闭灯光
			String closeClock = "off";
			try {
				mConnectedThread.writeChar(closeClock);
				mConnectedThread.writeChar(closeClock);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_openExerise:
			// 娱乐开启
			String exerciseon = "exerciseon";
			try {
				mConnectedThread.writeChar(exerciseon);
				mConnectedThread.writeChar(exerciseon);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			break;
		case R.id.id_closeExerise:
			// 娱乐关闭
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
	 * 将所有的图片切换为暗色的
	 */
	private void resetImg() {
		mWeixinImg.setImageResource(R.drawable.tab_weixin_normal);
		mFrdImg.setImageResource(R.drawable.tab_find_frd_normal);
		mSettingImg.setImageResource(R.drawable.tab_settings_normal);
	}

	/**
	 * 更新设备连接信息
	 */
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			/*********************************** 更新UI ************************************************/
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
			// 处理数据
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