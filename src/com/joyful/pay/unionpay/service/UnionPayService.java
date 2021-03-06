package com.joyful.pay.unionpay.service;

import java.util.HashMap;
import java.util.Map;

import com.joyful.pay.common.IPayBase;
import com.joyful.pay.common.PayStateEnum;
import com.joyful.pay.unionpay.sdk.AcpService;
import com.joyful.pay.unionpay.sdk.LogUtil;
import com.joyful.pay.unionpay.sdk.SDKConfig;
/**
 * 银联支付
 * @author hechangzhi
 * @version 创建时间：2016年8月24日
 */
public class UnionPayService implements IPayBase{

	/**
	 * 银联支付	返回结果
	 */
	@Override
	public Map<String, String> pay(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String merId = (String) map.get("merId");
		String txnAmt = (String) map.get("txnAmt");
		String orderId = (String) map.get("orderId");
		String txnTime = (String) map.get("txnTime");
		
		Map<String, String> contentData = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		contentData.put("version", DemoBase.version);            //版本号 全渠道默认值
		contentData.put("encoding", DemoBase.encoding_UTF8);     //字符集编码 可以使用UTF-8,GBK两种方式
		contentData.put("signMethod", "01");           		 	//签名方法 目前只支持01：RSA方式证书加密
		contentData.put("txnType", "01");              		 	//交易类型 01:消费
		contentData.put("txnSubType", "01");           		 	//交易子类 01：消费
		contentData.put("bizType", "000201");          		 	//填写000201
		contentData.put("channelType", "08");          		 	//渠道类型 08手机

		/***商户接入参数***/
		contentData.put("merId", merId);   		 				//商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		contentData.put("accessType", "0");            		 	//接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
		contentData.put("orderId", orderId);        	 	    //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则	
		contentData.put("txnTime", txnTime);		 		    //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		contentData.put("accType", "01");					 	//账号类型 01：银行卡02：存折03：IC卡帐号类型(卡介质)
		contentData.put("txnAmt", txnAmt);						//交易金额 单位为分，不能带小数点
		contentData.put("currencyCode", "156");                 //境内商户固定 156 人民币
		//contentData.put("reqReserved", "透传字段");              //商户自定义保留域，交易应答时会原样返回
		
		//后台通知地址（需设置为外网能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，【支付失败的交易银联不会发送后台通知】
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200或302，那么银联会间隔一段时间再次发送。总共发送5次，银联后续间隔1、2、4、5 分钟后会再次通知。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		contentData.put("backUrl", DemoBase.backUrl);
		
		/**对请求参数进行签名并发送http post请求，接收同步应答报文**/
		Map<String, String> reqData = AcpService.sign(contentData,DemoBase.encoding_UTF8);			 //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();								 //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		Map<String, String> rspData = AcpService.post(reqData,requestAppUrl,DemoBase.encoding_UTF8);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, DemoBase.encoding_UTF8)){
				LogUtil.writeLog("验证签名成功");
				String respCode = rspData.get("respCode") ;
				if(("00").equals(respCode)){
					//成功,获取tn号
					//String tn = resmap.get("tn");
					//TODO
				}else{
					//其他应答码为失败请排查原因或做失败处理
					//TODO
				}
			}else{
				LogUtil.writeErrorLog("验证签名失败");
				//TODO 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
		}
//		String reqMessage = DemoBase.genHtmlResult(reqData);
//		String rspMessage = DemoBase.genHtmlResult(rspData);
//		resp.getWriter().write("请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
		return rspData;
	}

	@Override
	public String getPayState() {
		// TODO Auto-generated method stub
		return PayStateEnum.UNIONPAY.getKey();
	}

	/**
	 * 银联退款	返回结果
	 */
	@Override
	public Map<String, String> refundPay(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String origQryId = (String) map.get("origQryId");
		String txnAmt = (String) map.get("txnAmt");
		
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", DemoBase.version);               //版本号
		data.put("encoding", DemoBase.encoding_UTF8);             //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", "01");                        //签名方法 目前只支持01-RSA方式证书加密
		data.put("txnType", "04");                           //交易类型 04-退货		
		data.put("txnSubType", "00");                        //交易子类型  默认00		
		data.put("bizType", "000201");                       //业务类型
		data.put("channelType", "08");                       //渠道类型，07-PC，08-手机		
		
		/***商户接入参数***/
		data.put("merId", "777290058110097");                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改		
		data.put("orderId", DemoBase.getOrderId());          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		data.put("txnTime", DemoBase.getCurrentTime());      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效		
		data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）		
		data.put("txnAmt", txnAmt);                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额		
		//data.put("reqReserved", "透传信息");                    //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		data.put("backUrl", DemoBase.backUrl);               //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知
		
		/***要调通交易以下字段必须修改***/
		data.put("origQryId", origQryId);      //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
		
		/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
		Map<String, String> reqData  = AcpService.sign(data,DemoBase.encoding_UTF8);		//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String url = SDKConfig.getConfig().getBackRequestUrl();									//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		Map<String, String> rspData = AcpService.post(reqData, url,DemoBase.encoding_UTF8);//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, DemoBase.encoding_UTF8)){
				LogUtil.writeLog("验证签名成功");
				String respCode = rspData.get("respCode") ;
				if(("00").equals(respCode)){
					//交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
					//TODO
				}else if(("03").equals(respCode)||
						 ("04").equals(respCode)||
						 ("05").equals(respCode)){
					//后续需发起交易状态查询交易确定交易状态
					//TODO
				}else{
					//其他应答码为失败请排查原因
					//TODO
				}
			}else{
				LogUtil.writeErrorLog("验证签名失败");
				//TODO 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
		}
//		String reqMessage = DemoBase.genHtmlResult(reqData);
//		String rspMessage = DemoBase.genHtmlResult(rspData);
//		resp.getWriter().write("请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
		return rspData;
	}

}
