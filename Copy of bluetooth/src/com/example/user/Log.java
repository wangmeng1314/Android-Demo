package com.example.user;

import com.example.bluetooth.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ActionMenuView.ActionMenuChildView;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Log extends Activity implements OnClickListener {
	/**
	 * �ؼ�
	 */
	private EditText editUserName;
	private EditText editUserPass;
	private Button btnCancel;
	private Button btnOk;

	/**
	 * �޲ι����ʼ��
	 */
	public Log() {
		super();
		editUserName = (EditText) findViewById(R.id.id_LogUserName);
		editUserPass = (EditText) findViewById(R.id.id_LogUserPassword);
		btnCancel = (Button) findViewById(R.id.id_LogBtnCancel);
		btnOk = (Button) findViewById(R.id.id_LogBtnOk);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		// �ؼ���Ӽ����¼�
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.id_LogBtnOk) {
			// ִ�е�½У��,���Ƿ�ɹ���½
			Editable un = editUserName.getText();
			String username = un.toString();
			Editable up = editUserPass.getText();
			String userpass = up.toString();
			boolean result = new LogVerify().verify(username, userpass);
			if (result) {
				// �����µĽ���,�����������
			} else {
				// ��ʾ�û�,����������
				Toast.makeText(this, "�˺Ż����������!", Toast.LENGTH_SHORT).show();
				editUserName.clearComposingText();
				editUserPass.clearComposingText();
			}
		} else {
			// ������һ������
		}
	}

}
