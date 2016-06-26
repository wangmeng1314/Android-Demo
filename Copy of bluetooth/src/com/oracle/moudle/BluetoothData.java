package com.oracle.moudle;

/**
 * 实现数据的存储的类,界面在更新时所取到的数据就来源于此
 * 
 * @author Administrator
 * 
 */
public class BluetoothData {
	// 红外数据 有人为0xef 无人为0xcd
	private String havePerson = "";
	// 烟雾标志 有烟为1 无烟为2
	private String haveSmoke = "";
	// 下雨标志 下雨为1 不下雨为2
	private String haveRain = "";
	// 温度
	private int temperature = 0;
	// 湿度
	private int humidity = 0;

	public String getHavePerson() {
		return havePerson;
	}

	public void setHavePerson(String havePerson) {
		this.havePerson = havePerson;
	}

	public String getHaveSmoke() {
		return haveSmoke;
	}

	public void setHaveSmoke(String haveSmoke) {
		this.haveSmoke = haveSmoke;
	}

	public String getHaveRain() {
		return haveRain;
	}

	public void setHaveRain(String haveRain) {
		this.haveRain = haveRain;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		return "BluetoothData [havePerson=" + havePerson + ", haveSmoke="
				+ haveSmoke + ", haveRain=" + haveRain + ", temperature="
				+ temperature + ", humidity=" + humidity + "]";
	}
}
