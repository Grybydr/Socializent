package com.socializent.application.socializent;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by Toshıba on 3/11/2017.
 */

public class HomePage extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.AppTheme);



        setContentView(R.layout.home_page);

    }

}
