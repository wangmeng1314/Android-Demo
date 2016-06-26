package com.example.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共的工具类
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
	 * 将字节转换为16进制
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
		// 温湿度
		String resultTem = "当前温度为:" + tem + "  ----  " + "当前湿度为:" + hum;
		// 红外传感器
		resultHavePerson = "";
		if (havePerson.equals("")) {
			resultHavePerson = "传感器出现故障";
		} else {
			macAddress = havePerson.substring(3, 19);
			if (!havePerson.equals("")
					&& havePer.equalsIgnoreCase(havePerson.substring(0, 2))) {
				resultHavePerson = "mac地址为" + macAddress + "的节点有人侵入！";
			} else if (!havePerson.equals("")
					&& noPer.equalsIgnoreCase(havePerson.substring(0, 2))) {
				resultHavePerson = "无物体侵入!";
			} else {
				resultHavePerson = "传感器出现故障！";
			}
		}

		// 烟雾和雨滴传感器
		resultHaveRain = "";
		resultHaveSmoke = "";
		if (haveRa.equalsIgnoreCase(haveRain)) {
			resultHaveRain = "是";
		} else if (noRain.equalsIgnoreCase(haveRain)) {
			resultHaveRain = "否";
		} else {
			resultHaveRain = "传感器出现异常";
		}
		if (haveSmo.equalsIgnoreCase(haveSmoke)) {
			resultHaveSmoke = "是";
		} else if (noSmo.equalsIgnoreCase(haveSmoke)) {
			resultHaveSmoke = "否";
		} else {
			resultHaveSmoke = "传感器出现异常";
		}
		finalResult = "";
		finalResult = "是否下雨:" + CommonUtil.resultHaveRain + " ---- " + "是否有烟雾:"
				+ resultHaveSmoke;
		map.put("tem", resultTem);
		map.put("person", resultHavePerson);
		map.put("smoke", finalResult);
		return map;
	}

	/**
	 * 将16进制转化为10进制的数
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
