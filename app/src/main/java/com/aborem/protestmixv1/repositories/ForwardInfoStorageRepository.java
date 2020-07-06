package com.aborem.protestmixv1.repositories;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Alternative to `repositories/ForwardInfoRepository` that uses internal storage rather than
 * database
 */
public class ForwardInfoStorageRepository {
    private final String FILENAME = "forwardInfoStorage";
    private Context context;

    public ForwardInfoStorageRepository(Context context) {
        this.context = context;
    }

    /**
     * Appends `phoneNumber` to end of file
     * @param phoneNumber the phone number that new messages should be forwarded to
     * @return boolean indicating if adding was successful (true) or not (false)
     */
    public boolean addForwardPhoneNumber(String phoneNumber) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write((phoneNumber + "\n").getBytes());
            return true;
        } catch (IOException e) {
            Toast.makeText(context, "Could not store phone info", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Finds the most recently added phone number by reading last line of file
     * TODO find better way of structuring this so that we don't have to read entire file?
     * @return the most recently added phone number
     */
    public String getMostRecentForwardPhoneNumber() {
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader reader;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            }
            // todo there's probably a much better way of doing this but i don't know it atm
            String lastLine = "";
            String currentLine = "";
            while ((currentLine = reader.readLine()) != null) {
                lastLine = currentLine;
            }
            return lastLine;
        } catch (IOException e) {
            Toast.makeText(context, "Could not find phone info", Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    public void deleteAll() {
        context.deleteFile(getFileName());
    }

    /**
     * Cleans file name by getting app directory. Maybe unnecessary?
     * @return the cleaned file name where phone numbers are stored
     */
    private String getFileName() {
        return context.getFilesDir() + "/" + FILENAME;
    }
}
