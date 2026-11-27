package com.cyberstep.sanriocharacters;

import android.os.Bundle;
import ls.sakana.SakanaActivity;

/* loaded from: classes.dex */
public class SushiDashActivity extends SakanaActivity {
    @Override // ls.sakana.SakanaActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        System.loadLibrary("smartar");
        requestWindowFeature(1);
        getWindow().addFlags(1024);
        super.onCreate(savedInstanceState);
    }
}
