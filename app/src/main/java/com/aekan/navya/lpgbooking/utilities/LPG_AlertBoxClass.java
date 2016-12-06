package com.aekan.navya.lpgbooking.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.StrictMode;

/**
 * Created by arunramamurthy on 17/04/16.
 */
public class LPG_AlertBoxClass extends DialogFragment {

    //create members to support Dialog options;
    public String ALERTDIALOGTITLE;
    public String SETPOSITIVETEXT;
    public String SETNEGATIVETEXT;
    public DialogInterface.OnClickListener SETPOSITIVECLICKLISTENER;
    public DialogInterface.OnClickListener SETNEGATIVECLICKLISTENER;

    public Dialog onCreateDialog(Bundle SavedInstance){
        //Create the dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //set dialog Title, positive and negative strings;
        if (SETNEGATIVECLICKLISTENER != null) {
            builder.setTitle(ALERTDIALOGTITLE).setPositiveButton(SETPOSITIVETEXT, SETPOSITIVECLICKLISTENER).setNegativeButton(SETNEGATIVETEXT, SETNEGATIVECLICKLISTENER);
        }
        else {
            builder.setTitle(ALERTDIALOGTITLE).setNeutralButton(SETPOSITIVETEXT,SETPOSITIVECLICKLISTENER);
        }
        //return the Dialog box;
        return builder.create();
    }

    public LPG_AlertBoxClass showDialogHelper(String Title, String PositiveText, String NegativeText, DialogInterface.OnClickListener PositiveClickListener,DialogInterface.OnClickListener NegativeClickListener){
        //Set the positive and negative texts
        ALERTDIALOGTITLE = Title;
        SETPOSITIVETEXT = PositiveText;
        SETNEGATIVETEXT = NegativeText;
        SETPOSITIVECLICKLISTENER = PositiveClickListener;
        SETNEGATIVECLICKLISTENER = NegativeClickListener;

        //Show the dialog;
       // super.show(getFragmentManager(),"Dialog");
        return this;


    }
}
