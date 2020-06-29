package com.aborem.protestmixv1;

import android.app.Application;
import android.content.Context;

// todo I think this is very bad but at the moment I don't know how to get around it
// todo remove from AndroidManifest as well when found better approach
public class ApplicationWrapper extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationWrapper.application = this;
    }

    public static Application getApplication() {
        return application;
    }

}
