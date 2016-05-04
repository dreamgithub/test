package com.lovebaby.util;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;

public class PushForIOS {
	public static void Push(String deviceToken,String state,String content,String id,String time,int badge){
		 try
	        {
	            //�ӿͻ��˻�ȡ��deviceToken���ڴ�Ϊ�˲��Լ򵥣�д�̶���һ�������豸��ʶ��
	          //String deviceToken = "df779eda 73258894 5882ec78 3ac7b254 6ebc66fe fa295924 440d34ad 6505f8c4";
	        	//String deviceToken = "cdae06d7 1269f0b4 641ced78 f9b8bd0a 4cd3046b c2be9271 9a18a2f0 916e4027";

	            System.out.println("Push Start deviceToken:" + deviceToken);
	            //������Ϣģʽ
	            PayLoad payLoad = new PayLoad();
	            payLoad.addAlert("�¹��棺"+content);
	            payLoad.addBadge(badge);//��Ϣ���ͱ����С��Ȧ����ʾ�����֡�
	            payLoad.addSound("default");
	            payLoad.addCustomDictionary("state", state);
	            payLoad.addCustomDictionary("content", content);
	            payLoad.addCustomDictionary("id", id);
	            payLoad.addCustomDictionary("time", time.substring(0, 10));
	            //ע��deviceToken
	            PushNotificationManager pushManager = PushNotificationManager.getInstance();
	            pushManager.addDevice("iPhone", deviceToken);
	            //����APNS
	            String host = "gateway.sandbox.push.apple.com";//���Ͳ��Ե�ַ
	            //String host = "gateway.push.apple.com";
	            int port = 2195;
	            String certificatePath = "C:/apns-tcs-pro.p12";//ǰ����ɵ�����JAVA��̨����APNS�����*.p12�ļ�λ��
	            String certificatePassword = "123456";//p12�ļ����롣
	            pushManager.initializeConnection(host, port, certificatePath, certificatePassword, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
	            //��������
	            Device client = pushManager.getDevice("iPhone");
	            System.out.println("������Ϣ: " + client.getToken()+"\n"+payLoad.toString() +" ");
	            pushManager.sendNotification(client, payLoad);
	            //ֹͣ����APNS
	            pushManager.stopConnection();
	            //ɾ��deviceToken
	            pushManager.removeDevice("iPhone");
	        }
	        catch (Exception ex)
	        {
	            ex.printStackTrace();
	        }
	}
}
