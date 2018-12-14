package link.app.byunm.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import link.app.byunm.Activity.IntroActivity;
import link.app.byunm.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            /****** 서버에서 전송한 데이터를 추출한다 *********/
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String url = remoteMessage.getData().get("url");

            // 상태바 영역에 공지 등록 요청 --> 파라미터는 상황에 따라 수정해야 함.
//            sendNotification(title, message, url);
            sendNotification_bic_picture(title, message, url);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param message FCM message body received.
     */
    private void sendNotification(String title, String message, String url) {

        Intent intent = new Intent(this, IntroActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("url", url);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int)(Math.random()*10000), notificationBuilder.build());

        /*if ( "퀴즈몰 쪽지".equals(title) || "모두의이상형 채팅".equals(title) ) {
            Intent popupIntent = new Intent(this, PushNotiActivity.class);
            popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            popupIntent.putExtra("title", title);
            popupIntent.putExtra("message", message);
            popupIntent.putExtra("url", url);
            startActivity(popupIntent);
        }*/
    }

    @SuppressLint("WrongConstant")
    private void sendNotification_bic_picture(String title, String message, String url){
        try{
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(this, IntroActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(this);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_push));
            builder.setSmallIcon(R.drawable.ic_push);
            builder.setTicker(getString(R.string.activity_quizready_live_survival28));
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setNumber(1);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.live_notification);
            Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(builder);
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(message);
            bigPictureStyle.bigPicture(bitmap); // 아래로 밀면 보여질 그림
            builder.setStyle(bigPictureStyle);
            notificationManager.notify(999, builder.build());
        }catch (NullPointerException e){
        }catch (Exception e){
        }
    }
}
