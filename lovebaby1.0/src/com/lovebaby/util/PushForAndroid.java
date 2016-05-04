package com.lovebaby.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;

import net.sf.json.JSONObject;
@SuppressWarnings("rawtypes")
public class PushForAndroid {
	static String appId = "S4UeZIX0cMAVD6j6NQP189";
	static String appkey = "kHLTnNmcpO69PJxKcDLhM3";
	static String master = "6kev9x0rPA8xQ8ZMEg9yi7";
	//static String CID = "077c649a73e3cf88b89ab09ab69e46b8";
	static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	
	@SuppressWarnings("unused")
	public static void Push(String deviceToken,String state,String content,String id,String time,int badge){
		IGtPush push = new IGtPush(host, appkey, master);
		try {
			push.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		NotificationTemplate  template = linkTemplateDemo(state,content,id,time,badge);
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
                //������Чʱ�䣬��λΪ���룬��ѡ
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);

		List targets = new ArrayList();
		Target target1 = new Target();
		Target target2 = new Target();

		target1.setAppId(appId);
		target1.setClientId(deviceToken);

		IPushResult ret = push.pushMessageToSingle(message, target1);
		System.out.println(ret.getResponse().toString());
	}
//	TransmissionTemplate
	@SuppressWarnings("unchecked")
	public static NotificationTemplate linkTemplateDemo(String state,String content,String id,String time,int badge) {
		NotificationTemplate  template = new NotificationTemplate ();
		// ����APPID��APPKEY
		template.setAppId(appId);
		template.setAppkey(appkey);
		// ����֪ͨ������������
		template.setTitle("�¹��棺");
		template.setText(content);
		// ����֪ͨ��ͼ��
		template.setLogo("icon.jpg");
		// ����֪ͨ������ͼ��
//		template.setLogoUrl("");
		// ����֪ͨ�Ƿ����壬�𶯣����߿����
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		// ���ô򿪵���ַ��ַ
//		template.setUrl("12132");
		// ͸����Ϣ����	          
        Map map=new HashMap();
        map.put("state", state);
        map.put("content", content);
        map.put("id", id);
        map.put("time", time.substring(0, 10));
        map.put("badge", badge);
        String jsonString=JSONObject.fromObject(map).toString();
		template.setTransmissionType(2);
		template.setTransmissionContent(jsonString);
		//template.setPushInfo("11", badge, "13213", "22");
		return template;
	}
}
