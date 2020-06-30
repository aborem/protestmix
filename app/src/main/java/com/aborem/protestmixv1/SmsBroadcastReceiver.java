package com.aborem.protestmixv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.aborem.protestmixv1.database.AppDatabase;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.models.ForwardInfo;
import com.aborem.protestmixv1.models.MessageModel;
import com.aborem.protestmixv1.repositories.ContactRepository;
import com.aborem.protestmixv1.repositories.ForwardInfoRepository;
import com.aborem.protestmixv1.repositories.MessageRepository;
import com.aborem.protestmixv1.util.ProtestMixUtil;

import java.util.List;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    /**
     * Forwards `smsMessage` to phone number in database and removes forward indicator
     * @param messageContent the content to write to the destination phone number
     * @return a boolean indicating if the forwarding was successful
     */
    private boolean forwardMessage(String messageContent) {
        ForwardInfoRepository repository = new ForwardInfoRepository(ApplicationWrapper.getApplication());
        List<ForwardInfo> forwardInfoList = repository.getAllForwardContacts().getValue();
        if (forwardInfoList != null && !forwardInfoList.isEmpty()) {
            // first entry will be most up to date
            ForwardInfo forwardInfoRow = forwardInfoList.get(0);
            SmsManager smsManager = SmsManager.getDefault();
            String fixedMessageContent = messageContent.substring(Constants.FORWARD_INDICATOR_LENGTH);
            smsManager.sendTextMessage(
                    forwardInfoRow.getForwardToPhoneNumber(),
                    null,
                    fixedMessageContent,
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
            Object[] smsObjects = (Object[]) intentExtras.get("pdus");
            if (smsObjects != null) {
                for (Object sm : smsObjects) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm);

                    String senderPhoneNumber =
                            ProtestMixUtil.formatPhoneNumber(smsMessage.getOriginatingAddress());
                    String messageContent = smsMessage.getMessageBody();

                    long messageSentAtMs = smsMessage.getTimestampMillis();
                    if (messageContent.startsWith(Constants.FORWARD_INDICATOR)) {
                        boolean forwardMessageSuccess = forwardMessage(messageContent);
                        if (!forwardMessageSuccess) {
                            // todo do something if failed?
                            abortBroadcast();
                            return;
                        }
                    }

                    ContactRepository contactRepository = new ContactRepository(ApplicationWrapper.getApplication());
                    Boolean senderInRecords = contactRepository.entryExists(senderPhoneNumber).getValue();
                    if (senderInRecords != null) {
                        contactRepository.insert(new ContactModel(senderPhoneNumber));
                    }

                    // Only add message to local db storage if not forwarded
                    MessageModel newMessage = new MessageModel(
                            senderPhoneNumber, messageContent, messageSentAtMs, false
                    );
                    MessageRepository messageRepository = new MessageRepository(
                            ApplicationWrapper.getApplication(), senderPhoneNumber
                    );
                    messageRepository.insert(newMessage);

                    abortBroadcast();
                }
            }
        }
    }
}
