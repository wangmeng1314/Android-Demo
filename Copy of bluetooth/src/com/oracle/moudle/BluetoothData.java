package com.oracle.moudle;

/**
 * ʵ�����ݵĴ洢����,�����ڸ���ʱ��ȡ�������ݾ���Դ�ڴ�
 * 
 * @author Administrator
 * 
 */
public class BluetoothData {
	// �������� ����Ϊ0xef ����Ϊ0xcd
	private String havePerson = "";
	// �����־ ����Ϊ1 ����Ϊ2
	private String haveSmoke = "";
	// �����־ ����Ϊ1 ������Ϊ2
	private String haveRain = "";
	// �¶�
	private int temperature = 0;
	// ʪ��
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
