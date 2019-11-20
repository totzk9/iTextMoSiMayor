package gte.com.itextmosimayor;

//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    private static final String TAG = "MyFirebaseMsgService";
//
//    /**
//     * Called when message is received.
//     *
//     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
//     */
//    // [START receive_message]
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // [START_EXCLUDE]
//        // There are two types of messages data messages and notification messages. Data messages
//        // are handled
//        // here in onMessageReceived whether the app is in the foreground or background. Data
//        // messages are the type
//        // traditionally used with GCM. Notification messages are only received here in
//        // onMessageReceived when the app
//        // is in the foreground. When the app is in the background an automatically generated
//        // notification is displayed.
//        // When the user taps on the notification they are returned to the app. Messages
//        // containing both notification
//        // and data payloads are treated as notification messages. The Firebase console always
//        // sends notification
//        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
//        // [END_EXCLUDE]
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            sendNotification(remoteMessage);
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null)
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }
//
//    /**
//     * Create and show a simple notification containing the received FCM message.
//     *
//     * @param messageBody FCM message body received.
//     */
//    private void sendNotification(RemoteMessage messageBody) {
//        final String mTitle = messageBody.getData().get("message_title");
//        final String mMessage = messageBody.getData().get("message");
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("default",
//                    "YOUR_CHANNEL_NAME",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
//            mNotificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
//                .setSmallIcon(R.mipmap.ic_envelope) // notification icon
//                .setContentTitle(mTitle) // title for notification
//                .setContentText(mMessage)// message for notification
//                .setSound(defaultSoundUri) // set alarm sound for notification
//                .setAutoCancel(true); // clear notification after click
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(pi);
//        mNotificationManager.notify(0, mBuilder.build());
//
////        MyHandlerThread handlerThread = new MyHandlerThread("MyHandlerThread");
////        handlerThread.start();
////        Handler handler = new Handler(handlerThread.getLooper());
////        handler.post(new Runnable() {
////            @Override
////            public void run() {
////                DatabaseHandler db = DatabaseHandler.newInstance(MainActivity.getMainActivity());
////                ContentValues cv = new ContentValues();
////                cv.put(NOTI_MSG_BODY, mMessage);
////                cv.put(NOTI_MSG_TITLE, mTitle);
////                cv.put(NOTI_DATETIME_RECEIVED, getDateTime());
////                db.insertNotification(cv);
////            }
////        });
//    }
//
//
////    private class MyHandlerThread extends HandlerThread {
////
////        Handler handler;
////
////        public MyHandlerThread(String name) {
////            super(name);
////        }
////
////        @Override
////        protected void onLooperPrepared() {
////            handler = new Handler(getLooper()) {
////                @Override
////                public void handleMessage(Message msg) {
////                    Log.i("handleMessage","" + msg);
////                    // process incoming messages here
////                    // this will run in non-ui/background thread
////                }
////            };
////        }
////    }
//
//}
