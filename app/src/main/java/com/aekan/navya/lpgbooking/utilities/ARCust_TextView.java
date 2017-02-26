package com.aekan.navya.lpgbooking.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.aekan.navya.lpgbooking.R;

/**
 * Created by arunramamurthy on 11/02/17.
 * This is custom text view to be used in app
 */

public class ARCust_TextView extends TextView {
    //custom constructor
    public ARCust_TextView(Context context){
        super(context);
        //this constructor just calls the super constructor

    }

    public ARCust_TextView(Context context, AttributeSet attributeSet){
        //call the super constructor first

        super(context,attributeSet);
        Log.v("Custom", " Beginning");
        //get Typed Array
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.ARCust_TextView,0,0);

        //Get the font family
        String fontTypeFace = new String() ;

        switch (attrsArray.getInteger(R.styleable.ARCust_TextView_fonttypeFace,0)) {
            case 0:
                fontTypeFace = ".ttf";
                break;
            case 1:
                fontTypeFace = "-Bold.ttf";
                break;
            case 2:
                fontTypeFace = "-BoldItalic.ttf";
                break;
            case 3:
                fontTypeFace = "-Italic.ttf";
                break;
            default:
                fontTypeFace = ".ttf";
                break;
        }


        //Get the text typeface
        String fontFamily = new String(); //fontFamily

           switch ( attrsArray.getInteger(R.styleable.ARCust_TextView_font,0)) {
               case 0:
                   fontFamily = "Junicode";
                   break;
               default:
                   fontFamily = "Junicode";
                   break;
           }
        //Get the file name to retrieve
        String filename = "fonts/" + fontFamily + fontTypeFace ;
        Log.v("Custom ", filename);
        Typeface selectedTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Junicode.ttf");
        Log.v("Custom ", "Before Typeface set");
        try {
             selectedTypeFace = Typeface.createFromAsset(context.getAssets(), filename);
        } catch (Exception e){
            selectedTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Junicode.ttf");
        }
        //set the font
        setTypeface(selectedTypeFace);

        //recycle typed array without fail
        attrsArray.recycle();

//        this.setGravity(Gravity.CENTER_VERTICAL);


    }


    public ARCust_TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);

        //get Typed Array
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ARCust_TextView,0,0);

        //Get the font family
        String fontTypeFace = new String() ;

        switch (attrsArray.getInteger(R.styleable.ARCust_TextView_fonttypeFace,0)) {
            case 0:
                fontTypeFace = ".ttf";
                break;
            case 1:
                fontTypeFace = "-Bold.ttf";
                break;
            case 2:
                fontTypeFace = "-BoldItalic.ttf";
                break;
            case 3:
                fontTypeFace = "-Italic.ttf";
                break;
            default:
                fontTypeFace = ".ttf";
                break;
        }


        //Get the text typeface
        String fontFamily = new String(); //fontFamily

        switch ( attrsArray.getInteger(R.styleable.ARCust_TextView_font,0)) {
            case 0:
                fontFamily = "Juniper";
                break;
            default:
                fontFamily = "Juniper";
                break;
        }
        //Get the file name to retrieve
        String filename = fontFamily + fontTypeFace ;
        Typeface selectedTypeFace = Typeface.createFromAsset(context.getAssets(), "Juniper.ttf");
        try {
            selectedTypeFace = Typeface.createFromAsset(context.getAssets(), filename);
        } catch (Exception e){
            selectedTypeFace = Typeface.createFromAsset(context.getAssets(), "Juniper.ttf");
        }
        //set the font
        setTypeface(selectedTypeFace);

        //recycle typed array without fail
        attrsArray.recycle();

        //set gravity
        this.setGravity(Gravity.CENTER_VERTICAL);



    }

}
