package com.example.service;

import java.util.HashMap;
import java.util.Map;

/**
 * �����Ĺ�����
 * 
 * @author fulei.yang
 * 
 */
public class CommonUtil {
	private static String noPer = "cd";
	private static String havePer = "ef";
	private static String noSmo = "ee";
	private static String haveSmo = "dd";
	private static String noRain = "aa";
	private static String haveRa = "cc";
	private static String resultHavePerson;
	private static String resultHaveRain;
	private static String resultHaveSmoke;
	private static String finalResult;
	private static String macAddress;

	/**
	 * ���ֽ�ת��Ϊ16����
	 * 
	 * @param src
	 * @return stringBuilder
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static Map<String, String> disposeData(String havePerson,
			String haveRain, String haveSmoke, int tem, int hum) {
		Map<String, String> map = new HashMap<String, String>();
		// ��ʪ��
		String resultTem = "��ǰ�¶�Ϊ:" + tem + "  ----  " + "��ǰʪ��Ϊ:" + hum;
		// ���⴫����
		resultHavePerson = "";
		if (havePerson.equals("")) {
			resultHavePerson = "���������ֹ���";
		} else {
			macAddress = havePerson.substring(3, 19);
			if (!havePerson.equals("")
					&& havePer.equalsIgnoreCase(havePerson.substring(0, 2))) {
				resultHavePerson = "mac��ַΪ" + macAddress + "�Ľڵ��������룡";
			} else if (!havePerson.equals("")
					&& noPer.equalsIgnoreCase(havePerson.substring(0, 2))) {
				resultHavePerson = "����������!";
			} else {
				resultHavePerson = "���������ֹ��ϣ�";
			}
		}

		// �������δ�����
		resultHaveRain = "";
		resultHaveSmoke = "";
		if (haveRa.equalsIgnoreCase(haveRain)) {
			resultHaveRain = "��";
		} else if (noRain.equalsIgnoreCase(haveRain)) {
			resultHaveRain = "��";
		} else {
			resultHaveRain = "�����������쳣";
		}
		if (haveSmo.equalsIgnoreCase(haveSmoke)) {
			resultHaveSmoke = "��";
		} else if (noSmo.equalsIgnoreCase(haveSmoke)) {
			resultHaveSmoke = "��";
		} else {
			resultHaveSmoke = "�����������쳣";
		}
		finalResult = "";
		finalResult = "�Ƿ�����:" + CommonUtil.resultHaveRain + " ---- " + "�Ƿ�������:"
				+ resultHaveSmoke;
		map.put("tem", resultTem);
		map.put("person", resultHavePerson);
		map.put("smoke", finalResult);
		return map;
	}

	/**
	 * ��16����ת��Ϊ10���Ƶ���
	 */
	public static int hexToTen(String hexString) {
		int result = 0;
		String r1 = hexString.substring(1, 2);
		String r2 = hexString.substring(0, 1);
		r1 = formatString(r1);
		r2 = formatString(r2);
		result = Integer.valueOf(r2) * 16 + Integer.valueOf(r1) * 1;
		return result;
	}

	private static String formatString(String r) {
		if ("a".equals(r)) {
			r = "10";
		}
		if ("b".equals(r)) {
			r = "11";
		}
		if ("c".equals(r)) {
			r = "12";
		}
		if ("d".equals(r)) {
			r = "13";
		}
		if ("e".equals(r)) {
			r = "14";
		}
		if ("f".equals(r)) {
			r = "15";
		}
		return r;
	}
}
