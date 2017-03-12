package com.aekan.navya.lpgbooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aekan.navya.lpgbooking.utilities.LPG_Utility;

/**
 * Created by aruramam on 3/8/2017.
 * This activity is provide an instruction to user
 * to allow permission to utilise phone features - "Call" or "SMS" functionalities
 * This activity will only be called from LPG booking activity,
 * if user has denied these permissions to this app.
 * This activity returns user's acceptance / refusal for permissions via activity result
 */

public class PermissionCheckForFeature extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //Call super class method
        super.onCreate(savedInstanceState);

        //set content layout for the activity
        setContentView(R.layout.permission_check_and_confirm);
        //Get the user components from layout
        TextView permissionBriefToUser = (TextView) findViewById(R.id.permission_user_intimation);
        Button buttonPermissionCancel = (Button) findViewById(R.id.button_permission_cancel);
        Button buttonPermssionOK = (Button) findViewById(R.id.button_permission_ok);

        //Set the appropriate user intimation message based on Intent value
        //This way, we can reuse the same enabler activity for both sms and call versions
        String permissionIntimationMessage = getIntent().getStringExtra(LPG_Utility.PERMISSION_INTIMATION_MESSAGE);
        switch (permissionIntimationMessage){
            case LPG_Utility.PERMISSION_CALL_INTIMATION:
                permissionBriefToUser.setText(getResources().getText(R.string.permission_intimation_user_for_call));
                break;
            case LPG_Utility.PERMISSION_SMS_INTIMATION:
                permissionBriefToUser.setText(getResources().getText(R.string.permission_intimation_user_for_sms));
                break;
            default:
                permissionBriefToUser.setText(getResources().getText(R.string.permission_intimation_user_for_call));
                break;

        }

        //set onclick listener for cancel button
        buttonPermissionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancel = new Intent();
                intentCancel.putExtra(LPG_Utility.PERMISSION_STATUS,LPG_Utility.PERMISSION_DENIED);
                setResult(Activity.RESULT_CANCELED,intentCancel);
            }

        });

        //set onClick listener for ok button
        buttonPermssionOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOK = new Intent();
                intentOK.putExtra(LPG_Utility.PERMISSION_STATUS,LPG_Utility.PERMISSION_DENIED);
                setResult(Activity.RESULT_OK,intentOK);
            }
        });
    }

}
