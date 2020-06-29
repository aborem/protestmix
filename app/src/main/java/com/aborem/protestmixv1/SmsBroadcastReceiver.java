package com.aborem.protestmixv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.aborem.protestmixv1.database.AppDatabase;
import com.aborem.protestmixv1.models.MessageModel;
import com.aborem.protestmixv1.repositories.MessageRepository;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private boolean shouldForwardMessage(SmsMessage smsMessage) {
        return smsMessage.getMessageBody().startsWith(Constants.FORWARD_INDICATOR);
    }

    /**
     * Forwards `smsMessage` to phone number in database and removes forward indicator
     * @param smsMessage SmsMessage received
     * @param appDb the app database, passed in so as not to create new, unnecessary instance
     * @return a boolean indicating if the forwarding was successful
     */
    private boolean forwardMessage(SmsMessage smsMessage, AppDatabase appDb) {
        // todo uncomment code when database is setup
//        List<ForwardInfo> forwardInfoList = appDb.forwardInfoDao().getAll();
        String[] messageContents = smsMessage.getMessageBody().split("\n");
//        if (!forwardInfoList.isEmpty()) {
//            ForwardInfo forwardInfoRow = forwardInfoList.get(0);

//            long expiration = forwardInfoRow.getExpirationTimeMs();
//            long now = System.currentTimeMillis();
//            if (expiration > now) {
//                return false;
//            }

            SmsManager smsManager = SmsManager.getDefault();
            String fixedMessageContent = messageContents[0].substring(Constants.FORWARD_INDICATOR.length());
            smsManager.sendTextMessage("5556", null, fixedMessageContent, null, null);
            return true;
//        }
//        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get("pdus");
            AppDatabase appDb = AppDatabase.getInstance(context);
            for (Object sm : sms) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm);
                if (shouldForwardMessage(smsMessage)) {
                    boolean forwardMessageSuccess = forwardMessage(smsMessage, appDb);
                    if (!forwardMessageSuccess) {
                        // todo do something if failed?
                        return;
                    }
                }
                // Only add message to local db storage if not forwarded
                MessageModel newMessage = new MessageModel(
                        "5554",
                        smsMessage.getMessageBody(),
                        System.currentTimeMillis(),
                        false
                );
                MessageRepository repository = new MessageRepository(ApplicationWrapper.getApplication(), "5554");
                repository.insert(newMessage);
                // todo figure out how to suppress actual text message being received
            }
        }
    }

//    /**
//     * Checks if application has permissions to read SMS by checking ContextCompat
//     * @return true if has permissions, false otherwise
//     */
//    private boolean hasReadSMSPermissions() {
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
//                == PackageManager.PERMISSION_GRANTED;
//    }


//    /**
//     * Requests read sms permissions from user and displays Toast
//     */
//    private void getPermissionToReadSMS() {
//        if (!hasReadSMSPermissions()) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
//                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
//            }
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSIONS_REQUEST);
//        }
//    }
}
