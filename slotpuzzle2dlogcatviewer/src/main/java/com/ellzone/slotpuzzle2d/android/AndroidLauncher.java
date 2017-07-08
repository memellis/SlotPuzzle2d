package com.ellzone.slotpuzzle2d.android;

import android.os.Bundle;
import android.os.Build;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import com.fatangare.logcatviewer.utils.LogcatViewer;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ellzone.slotpuzzle2d.SlotPuzzle;

public class AndroidLauncher extends AndroidApplication {
	public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        checkPermission();
        LogcatViewer.showLogcatLoggerView(this);
		initialize(new SlotPuzzle(), config);
	}
	
	    @Override
    protected void onDestroy() {
        LogcatViewer.closeLogcatLoggerView(this);
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission();

            } else {
                LogcatViewer.showLogcatLoggerView(this);
            }

        }
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }
}
