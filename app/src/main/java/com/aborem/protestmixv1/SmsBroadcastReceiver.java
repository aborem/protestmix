package com.aborem.protestmixv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.aborem.protestmixv1.Constants;
import com.aborem.protestmixv1.database.AppDatabase;
import com.aborem.protestmixv1.models.ForwardInfo;
import com.aborem.protestmixv1.models.MessageModel;

import java.util.Arrays;
import java.util.List;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private boolean shouldForwardMessage(SmsMessage smsMessage) {
        return smsMessage.getMessageBody().startsWith(Constants.FORWARD_INDICATOR);
    }

    private boolean forwardMessage(SmsMessage smsMessage, AppDatabase appDb) {
        List<ForwardInfo> forwardInfoList = appDb.forwardInfoDao().getAll();
        String[] messageContents = smsMessage.getMessageBody().split("\n");
        if (!forwardInfoList.isEmpty()) {
            ForwardInfo forwardInfoRow = forwardInfoList.get(0);

            long expiration = forwardInfoRow.getExpirationTimeMs();
            long now = System.currentTimeMillis();
            if (expiration > now) {
                return false;
            }

            SmsManager smsManager = SmsManager.getDefault();
            List<String> fixedMessageContent = Arrays.asList(messageContents)
                    .subList(1, messageContents.length);
            smsManager.sendTextMessage(
                    forwardInfoRow.getForwardToPhoneNumber(),
                    null,
                    TextUtils.join("\n", fixedMessageContent),
                    null,
                    null
            );
            return true;
        }
        return false;
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
                    if(forwardMessage(smsMessage, appDb)) {
                        // showing message if failed to send
                        return;
                    }
                }
                MessageModel newMessage = new MessageModel(
                        smsMessage.getOriginatingAddress(),
                        smsMessage.getMessageBody(),
                        System.currentTimeMillis(),
                        false
                );
                appDb.messageDao().insertAll(newMessage);
            }
        }
    }
}
