package dakewe.com.mqtttest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class PushCallback implements MqttCallback {

    private ContextWrapper context;
    private static final String TAG = "PushCallback";

    public PushCallback(ContextWrapper context) {

        this.context = context;

    }

    @Override
    public void connectionLost(Throwable cause) {
        //连接断开时的回调方法，可以在这里重新连接
    }


	@Override
    public void messageArrived(String s, MqttMessage message) throws Exception {
//    	有新消息到达时的回调方法
        Log.d(TAG, "messageArrived: 收到消息:"+new String(message.getPayload()));
        final NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        final Notification notification;


        final Intent intent = new Intent(context, BlackIceActivity.class);
        final PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle("Message")
                .setContentText(new String(message.getPayload()) + " ")
                .setContentIntent(activity)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true);
        notification=builder.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.number += 1;
        notificationManager.notify(0, notification);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //成功发布某一消息后的回调方法
    }
}
