/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.ellzone.slotpuzzle2d.prototypes.android;

import java.util.List;
import android.os.Build;
import android.annotation.TargetApi;
import android.net.Uri;
import android.provider.Settings;
import com.fatangare.logcatviewer.utils.LogcatViewer;
import com.ellzone.slotpuzzle2d.prototypes.SPPrototypes;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AndroidSPPrototypeStarter extends ListActivity {
	public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;
	SharedPreferences prefs;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkPermission();
        LogcatViewer.showLogcatLoggerView(this);
		prototypeStarter();
	}
	
    @Override
    protected void onDestroy() {
        LogcatViewer.closeLogcatLoggerView(this);
        super.onDestroy();
    }
	
	private void prototypeStarter() {
		List<String> testNames = SPPrototypes.getNames();
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testNames));

		prefs = getSharedPreferences("libgdx-tests", Context.MODE_PRIVATE);
		getListView().setBackgroundColor(Color.BLACK);
		getListView().setSelectionFromTop(prefs.getInt("index", 0), prefs.getInt("top", 0));
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
				prototypeStarter();
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

	protected void onListItemClick (ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		Editor editor = prefs.edit();
		editor.putInt("index", listView.getFirstVisiblePosition());
		editor.putInt("top", listView.getChildAt(0) == null ? 0 : listView.getChildAt(0).getTop());
		editor.commit();

		Object o = this.getListAdapter().getItem(position);
		String spPrototypeName = o.toString();

		Bundle bundle = new Bundle();
		bundle.putString("spprototype", spPrototypeName);
		Intent intent = new Intent(this, SPPrototypeActivity.class);
		intent.putExtras(bundle);

		startActivity(intent);
	}

}
