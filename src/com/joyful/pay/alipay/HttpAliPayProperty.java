package com.joyful.pay.alipay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joyful.pay.util.PropUtil;

/**
 * 支付宝的根据properties的配置拿到对应的变化值
 * @author hechangzhi
 * @version 创建时间：2016年8月23日
 */
public class HttpAliPayProperty {
	
	private static Map<String,String> paramsMap = new HashMap<String,String>();

	private static Logger log = (Logger) LoggerFactory.getLogger(HttpAliPayProperty.class);

	static {
		Properties prop = PropUtil.getUrlProperties("property/alipay.properties");
		// 返回Properties中包含的key-value的Set视图
		Set<Entry<Object, Object>> set = prop.entrySet();
		// 返回在此Set中的元素上进行迭代的迭代器
		Iterator<Map.Entry<Object, Object>> it = set.iterator();
		String key = null, value = null;
		// 循环取出key-value
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			key = String.valueOf(entry.getKey());
			value = String.valueOf(entry.getValue());
			key = key == null ? key : key.trim().toUpperCase();
			value = value == null ? value : value.trim();
			// 将key-value放入map中
			paramsMap.put(key, value);
		}
	}
	
	public static String getVal(String key) {
		key = key == null ? "" : key.trim().toUpperCase();
		String temp = paramsMap.get(key);
		if(null==temp){
			try {
				throw new Exception(key+"从 property/alipay.properties 取值没有成功...");
			} catch (Exception e) {
				log.error(key+"从 property/alipay.properties 取值没有成功...");
				e.printStackTrace();
			}
		}
		return temp;
	}

	private static String backAliPayUrl = null;//"http://localhost:8080/joyful-gateway";
	private static String joyfulAliPayUrl = null;//"http://localhost:8080/joyful-pay";

	/**
	 * 付款成功之后返回的跳转地址
	 * @return
	 */
	public static String getBackAliPayUrl() {
		if(null==backAliPayUrl){
			backAliPayUrl = getVal("backAliPayUrl");
		}  
		return backAliPayUrl;
	}
	
	/**
	 * 付款地址url
	 * @return
	 */
	public static String getJoyfulAliPayUrl() {
		if(null==joyfulAliPayUrl){
			joyfulAliPayUrl = getVal("joyfulAliPayUrl");
		}  
		return joyfulAliPayUrl;
	}
}
