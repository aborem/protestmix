package com.aborem.protestmixv1.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.aborem.protestmixv1.MainActivity;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get("pdus");
            StringBuilder smsMessageStr = new StringBuilder();
            for (Object sm : sms) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm);

                String smsBody = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr.append("SMS From: ").append(address).append("\n");
                smsMessageStr.append(smsBody).append("\n");
            }

            // todo add message to local storage and put notification on row corresponding to chat
        }
    }
}
