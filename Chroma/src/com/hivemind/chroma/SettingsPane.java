package com.hivemind.chroma;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SettingsPane extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pane);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings_pane, menu);
        return true;
    }
}
