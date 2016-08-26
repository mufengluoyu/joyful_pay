package com.joyful.pay.common;

/**
 * 支付方式枚举类
 * @author hechangzhi
 * @version 创建时间：2016年8月23日
 */
public enum PayStateEnum {
	WXPAY("WXPAY","微信支付"),UNIONPAY("UNIONPAY","银联支付"),ALIPAY("ALIPAY","支付宝支付");
	
	private String key;
	private String value;
	
	PayStateEnum(String key, String value){
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
