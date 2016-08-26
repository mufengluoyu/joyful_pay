package com.joyful.pay.common;

import java.util.Map;

public interface IPayBase {
	/**
	 * 手机app的付款返回数据
	 * @param map
	 * @return
	 */
	public Map<String,String> pay(Map<String,Object> map);
	/**
	 * 返回类型是"WXPAY","ALIPAY","UNIONPAY"
	 * @return
	 */
	public String getPayState();
	
	/**
	 * 手机app退款返回的数据
	 * @param map
	 * @return
	 */
	public Map<String,String> refundPay(Map<String,Object> map);
}
