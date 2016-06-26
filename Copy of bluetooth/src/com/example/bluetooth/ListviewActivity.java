package com.example.bluetooth;

import java.io.File;

import java.io.IOException;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 我必须把你的最大丑闻写在这里,那就是创建文件没有给系统加入相应的权限.
 * 必须还要强调的就是界面只去做界面该做的事情,你这里的话把数据的接受和发送都放到了这里 两个内部类应该从属于展示数据的界面,
 * 故而必须建立一个公用的类,存储需要用到的连接和数据
 * 
 * @author Administrator
 * 
 */
public class ListviewActivity extends Activity {
	public static String Tag = "ListviewActivity";
	// UI初始化
	private ListView listview;
	private Button btnsearch;
	// device硬件设备
	private BluetoothDevice device;
	// 获得的蓝牙连接
	private BluetoothSocket clientSocket;
	// 蓝牙适配器
	public BluetoothAdapter blu;
	// 数据存取目录
	File dir;
	// 数组适配器,显示到listview中去
	private ArrayAdapter<String> arr_adapter;

	/**
	 * 主要的初始化操作均放在此方法中
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		// 得到控件,UI初始化
		listview = (ListView) findViewById(R.id.listview);
		btnsearch = (Button) findViewById(R.id.btnOk);
		// 获得本地蓝牙适配器
		blu = BluetoothAdapter.getDefaultAdapter();
		Log.i(Tag, "listview初始化成功!");
		// 新建数据适配器
		arr_adapter = new ArrayAdapter<String>(ListviewActivity.this,
				android.R.layout.simple_list_item_1);
		// 使用视图加载listview
		listview.setAdapter(arr_adapter);
		System.out.println("onCreate()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 给查找蓝牙设备按钮添加点击事件
		btnsearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 使用意图注册,启动蓝牙扫描
				IntentFilter filter = new IntentFilter(
						BluetoothDevice.ACTION_FOUND);
				registerReceiver(bluetoothfoundReceiver, filter);
				arr_adapter.setNotifyOnChange(true);
				arr_adapter.clear();
				Toast.makeText(ListviewActivity.this, "蓝牙设备已经开始扫描!",
						Toast.LENGTH_LONG).show();
				// 蓝牙设备开始扫描
				blu.startDiscovery();
			}
		});

		// 为listview加上监听事件,在此应该创建连接
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				new connect().start();
			}
		});
		// 将数据写入文件中
		String path = Environment.getExternalStorageDirectory() + "/"
				+ "-----------------bluetoothdata----------------";
		dir = new File(path);
		// 创建文件保存的目录
		dir.mkdir();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 释放注册意图所占用的资源
		unregisterReceiver(bluetoothfoundReceiver);
	}

	/**
	 * 建立连接的线程
	 */
	private class connect extends Thread {
		@Override
		public void run() {
			super.run();
			// 得到设备
			try {
				clientSocket = device.createRfcommSocketToServiceRecord(UUID
						.fromString("00001101-0000-1000-8000-00805F9B34FB"));
				// 开始连接
				clientSocket.connect();
				// 得到连接以后将此连接传送给BlueToothData类
//				BluetoothData.bluSocket = clientSocket;
				//启动新的activity
				Intent intent = new Intent(ListviewActivity.this, MonitorPatternActivity.class);
				startActivity(intent);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("连接不成功!");
			}
		}
	}

	/**
	 * 匿名内部类,得到数据.
	 */
	private final BroadcastReceiver bluetoothfoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 如果可以匹配,则得到硬件
				device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				arr_adapter.add(device.getName() + "\n" + device.getAddress());
				// 更新数据
				arr_adapter.setNotifyOnChange(true);
				arr_adapter.notifyDataSetChanged();
				// 停止搜索
				blu.cancelDiscovery();
			}
		}
	};

}
