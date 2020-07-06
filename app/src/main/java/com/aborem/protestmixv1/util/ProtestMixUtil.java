package com.aborem.protestmixv1.util;

import android.app.Application;
import android.os.Build;
import android.telephony.PhoneNumberUtils;

import com.aborem.protestmixv1.repositories.ContactRepository;
import com.aborem.protestmixv1.repositories.ForwardInfoRepository;
import com.aborem.protestmixv1.repositories.ForwardInfoStorageRepository;
import com.aborem.protestmixv1.repositories.MessageRepository;

import java.util.Locale;

public class ProtestMixUtil {
    /**
     * Formates `rawPhoneNumber` based on current build
     * @param rawPhoneNumber string of raw number to format
     * @return formatted number under E164 standards
     */
    public static String formatPhoneNumber(String rawPhoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(rawPhoneNumber, Locale.getDefault().getCountry());
        } else {
            return PhoneNumberUtils.formatNumber(rawPhoneNumber);
        }
    }

    /**
     * Deletes all data in stored in the db. Option accessible through every screen.
     * @param application the application from which this function was called/button was triggered
     */
    public static void clearAllData(Application application) {
        clearContacts(application);
        clearForwardInfo(application);
        clearMessages(application);
    }

    /**
     * Deletes all entries in contacts table
     * @param application application
     */
    public static void clearContacts(Application application) {
        ContactRepository contactRepository = new ContactRepository(application);
        contactRepository.deleteAll();
    }

    /**
     * Deletes all entries in messages table
     * @param application application
     */
    public static void clearMessages(Application application) {
        MessageRepository messageRepository = new MessageRepository(application);
        messageRepository.deleteAll();
    }

    /**
     * Deletes all entries in forward_info table
     * @param application application
     */
    public static void clearForwardInfo(Application application) {
        ForwardInfoRepository forwardInfoRepository = new ForwardInfoRepository(application);
        forwardInfoRepository.deleteAll();

        ForwardInfoStorageRepository forwardInfoStorageRepository =
                new ForwardInfoStorageRepository(application.getApplicationContext());
        forwardInfoRepository.deleteAll();

    }

}
