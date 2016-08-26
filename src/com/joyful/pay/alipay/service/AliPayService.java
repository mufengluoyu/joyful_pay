package com.joyful.pay.alipay.service;

import java.util.HashMap;
import java.util.Map;

import com.joyful.pay.alipay.config.AlipayConfig;
import com.joyful.pay.alipay.util.UtilDate;
import com.joyful.pay.common.IPayBase;
import com.joyful.pay.common.PayStateEnum;
/**
 * 支付宝支付
 * @author hechangzhi
 * @version 创建时间：2016年8月24日
 */
public class AliPayService implements IPayBase{

	@Override
	public Map<String, String> pay( Map<String, Object> map) {
		// TODO Auto-generated method stub
		 //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = (String) map.get("out_trade_no");
		
        //订单名称，必填
        String subject = (String) map.get("subject");
		
        //付款金额，必填
        String total_fee =(String) map.get("total_fee");
		
        //收银台页面上，商品展示的超链接，必填	收银台页面上，商品展示的超链接。
        String show_url = (String) map.get("show_url");
		
        //商品描述，可空
        String body = (String) map.get("body");
		
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.notify_url);	//支付宝服务器主动通知商户网站里指定的页面http路径。
		sParaTemp.put("return_url", AlipayConfig.return_url);	//支付宝处理完请求后，当前页面自动跳转到商户网站里指定页面的http路径。
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("show_url", show_url);
		//sParaTemp.put("app_pay","Y");//启用此参数可唤起钱包APP支付。
		sParaTemp.put("body", body);
		//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.2Z6TSk&treeId=60&articleId=103693&docType=1
        //如sParaTemp.put("参数名","参数值");

		
		//建立请求
//		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
//		out.println(sHtmlText);
		return sParaTemp;
	}

	@Override
	public String getPayState() {
		// TODO Auto-generated method stub
		return PayStateEnum.ALIPAY.getKey();
	}

	/**
	 * 支付宝退款
	 * @param map  数据主要包含的数据：
	 * 					batch_no批次号，
	 * 					batch_num退款笔数，
	 * 					detail_data退款详情数据格式（支付宝交易号^退款金额^备注），多笔请用#隔开
	 */
	@Override
	public Map<String, String> refundPay(Map<String, Object> map) {
		// TODO Auto-generated method stub
		//批次号，必填，格式：当天日期[8位]+序列号[3至24位]，如：201603081000001
		String batch_no = (String) map.get("batch_no");
		//退款笔数，必填，参数detail_data的值中，“#”字符出现的数量加1，最大支持1000笔（即“#”字符出现的数量999个）
        String batch_num =(String) map.get("batch_num");
        //退款详细数据，必填，格式（支付宝交易号^退款金额^备注），多笔请用#隔开
        String detail_data = (String) map.get("detail_data");
        //把请求参数打包成数组
  		Map<String, String> sParaTemp = new HashMap<String, String>();
  		sParaTemp.put("service", "refund_fastpay_by_platform_pwd");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
  		sParaTemp.put("notify_url", AlipayConfig.notify_url);
  		sParaTemp.put("seller_user_id", AlipayConfig.seller_id);
  		sParaTemp.put("refund_date", UtilDate.getDateFormatter());
  		sParaTemp.put("batch_no", batch_no);
  		sParaTemp.put("batch_num", batch_num);
  		sParaTemp.put("detail_data", detail_data);
      		
  		//建立请求	生成业务请求
//  		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
        
		return sParaTemp;
	}
	
}
