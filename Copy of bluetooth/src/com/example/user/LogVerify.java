package com.example.user;

public class LogVerify {
	public boolean verify(String username, String userpass) {
		if (username.equals("�˺�:admin") && userpass.equals("����:123")) {
			return true;
		} else {
			return false;
		}
	}
}
