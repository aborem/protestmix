package com.aborem.protestmixv1.util;

import android.os.Build;
import android.telephony.PhoneNumberUtils;

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
}
