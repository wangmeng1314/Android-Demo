package com.example.bluetooth;

import com.example.user.LogVerify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity implements OnClickListener {
	EditText username;
	EditText userpass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ������������ʽ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		// �õ��ؼ�
		username = (EditText) findViewById(R.id.id_LogUserName);
		userpass = (EditText) findViewById(R.id.id_LogUserPassword);
		Button btnLogOk = (Button) findViewById(R.id.id_LogBtnOk);
		Button btnLogCancel = (Button) findViewById(R.id.id_LogBtnCancel);
		btnLogOk.setOnClickListener(this);
		btnLogCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_LogBtnOk:
			LogVerify log = new LogVerify();
			boolean result = log.verify(username.getText().toString(), userpass
					.getText().toString());
			if (result) {
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(LoginActivity.this, "�˺Ż��������д���",
						Toast.LENGTH_SHORT).show();
				// ����������û�����
				username.clearComposingText();
				userpass.clearComposingText();
			}
			break;
		case R.id.id_LogBtnCancel:
			Intent intent = new Intent(this, LeadMain.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
