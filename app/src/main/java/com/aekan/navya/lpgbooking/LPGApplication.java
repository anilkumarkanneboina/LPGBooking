package com.aekan.navya.lpgbooking;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.aekan.navya.lpgbooking.utilities.LPG_AlertBoxClass;
import com.aekan.navya.lpgbooking.utilities.LPG_SQLOpenHelperClass;

/**
 * Created by arunramamurthy on 17/04/16.
 */
public class LPGApplication extends Application {

    //Initialize global state objects and properties
    public SQLiteDatabase LPGDB;
    public LPG_AlertBoxClass LPG_Alert ;

    public void LPG_AlertBoxInstantiate(){

       // LPG_Alert.showDialogHelper("Setting up","Ok","Cancel",new DialogInterface.OnClickListener());

        LPGDB = (new LPG_SQLOpenHelperClass(getApplicationContext())).getWritableDatabase();
        LPG_Alert = new LPG_AlertBoxClass();

    }

}
