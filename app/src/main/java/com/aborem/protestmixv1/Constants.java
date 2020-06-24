package com.aborem.protestmixv1;

import java.util.UUID;

public class Constants {
    // todo eventually change to authenticated user
    public static final String authorId = UUID.randomUUID().toString();
    public static final String DB_NAME = "protest_mix_db";
    public static final String FORWARD_INDICATOR = "<<FORWARD_MESSAGE>>";
}
