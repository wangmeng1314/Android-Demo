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
 * �ұ�������������д������,�Ǿ��Ǵ����ļ�û�и�ϵͳ������Ӧ��Ȩ��.
 * ���뻹Ҫǿ���ľ��ǽ���ֻȥ���������������,������Ļ������ݵĽ��ܺͷ��Ͷ��ŵ������� �����ڲ���Ӧ�ô�����չʾ���ݵĽ���,
 * �ʶ����뽨��һ�����õ���,�洢��Ҫ�õ������Ӻ�����
 * 
 * @author Administrator
 * 
 */
public class ListviewActivity extends Activity {
	public static String Tag = "ListviewActivity";
	// UI��ʼ��
	private ListView listview;
	private Button btnsearch;
	// deviceӲ���豸
	private BluetoothDevice device;
	// ��õ���������
	private BluetoothSocket clientSocket;
	// ����������
	public BluetoothAdapter blu;
	// ���ݴ�ȡĿ¼
	File dir;
	// ����������,��ʾ��listview��ȥ
	private ArrayAdapter<String> arr_adapter;

	/**
	 * ��Ҫ�ĳ�ʼ�����������ڴ˷�����
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		// �õ��ؼ�,UI��ʼ��
		listview = (ListView) findViewById(R.id.listview);
		btnsearch = (Button) findViewById(R.id.btnOk);
		// ��ñ�������������
		blu = BluetoothAdapter.getDefaultAdapter();
		Log.i(Tag, "listview��ʼ���ɹ�!");
		// �½�����������
		arr_adapter = new ArrayAdapter<String>(ListviewActivity.this,
				android.R.layout.simple_list_item_1);
		// ʹ����ͼ����listview
		listview.setAdapter(arr_adapter);
		System.out.println("onCreate()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// �����������豸��ť��ӵ���¼�
		btnsearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// ʹ����ͼע��,��������ɨ��
				IntentFilter filter = new IntentFilter(
						BluetoothDevice.ACTION_FOUND);
				registerReceiver(bluetoothfoundReceiver, filter);
				arr_adapter.setNotifyOnChange(true);
				arr_adapter.clear();
				Toast.makeText(ListviewActivity.this, "�����豸�Ѿ���ʼɨ��!",
						Toast.LENGTH_LONG).show();
				// �����豸��ʼɨ��
				blu.startDiscovery();
			}
		});

		// Ϊlistview���ϼ����¼�,�ڴ�Ӧ�ô�������
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				new connect().start();
			}
		});
		// ������д���ļ���
		String path = Environment.getExternalStorageDirectory() + "/"
				+ "-----------------bluetoothdata----------------";
		dir = new File(path);
		// �����ļ������Ŀ¼
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
		// �ͷ�ע����ͼ��ռ�õ���Դ
		unregisterReceiver(bluetoothfoundReceiver);
	}

	/**
	 * �������ӵ��߳�
	 */
	private class connect extends Thread {
		@Override
		public void run() {
			super.run();
			// �õ��豸
			try {
				clientSocket = device.createRfcommSocketToServiceRecord(UUID
						.fromString("00001101-0000-1000-8000-00805F9B34FB"));
				// ��ʼ����
				clientSocket.connect();
				// �õ������Ժ󽫴����Ӵ��͸�BlueToothData��
//				BluetoothData.bluSocket = clientSocket;
				//�����µ�activity
				Intent intent = new Intent(ListviewActivity.this, MonitorPatternActivity.class);
				startActivity(intent);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("���Ӳ��ɹ�!");
			}
		}
	}

	/**
	 * �����ڲ���,�õ�����.
	 */
	private final BroadcastReceiver bluetoothfoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// �������ƥ��,��õ�Ӳ��
				device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				arr_adapter.add(device.getName() + "\n" + device.getAddress());
				// ��������
				arr_adapter.setNotifyOnChange(true);
				arr_adapter.notifyDataSetChanged();
				// ֹͣ����
				blu.cancelDiscovery();
			}
		}
	};

}
