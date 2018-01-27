package com.aekan.navya.lpgbooking.utilities;

import android.util.Log;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.GregorianCalendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by arunramamurthy on 27/01/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class LPG_UtilityTest {

    @Mock
    Log mLog;

    @Test
    public void getDateDiff_equalDays() throws Exception {
        //mock log functionality
       // when(Log.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,14)))).thenReturn(0);
       // when(Log.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,14)))).thenReturn(0);
        assertThat(
                "Both dates are equal ",
                LPG_Utility.getDateDiff(new GregorianCalendar(2018,0,14),new GregorianCalendar(2018,0,14)),
                equalTo(0)
        );

    }

    @Test
    public void getDateDiff_differenceOne() throws Exception{

        //mock Log.v
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,11)))).thenReturn(0);
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,10)))).thenReturn(0);
        assertThat(
                "Difference is one",
                LPG_Utility.getDateDiff(
                        new GregorianCalendar(2018,0,11),
                        new GregorianCalendar(2018,0,10)
                ),
                equalTo(-1)
        );
    }

    @Test
    public void getDateDiff_differencelastyear() throws Exception{

        //mock Log.v
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,11)))).thenReturn(0);
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,10)))).thenReturn(0);
        assertThat(
                "Current Date is Christmas 2017",
                LPG_Utility.getDateDiff(
                        new GregorianCalendar(2018,0,26),
                        new GregorianCalendar(2017,11,25)
                ),
                equalTo(-1)
        );
    }


    @Test
    public void getDateDiff_difference_justonedayearlier() throws Exception{

        //mock Log.v
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,11)))).thenReturn(0);
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,10)))).thenReturn(0);
        assertThat(
                "Current Date is just one day earlier",
                LPG_Utility.getDateDiff(
                        new GregorianCalendar(2018,0,26),
                        new GregorianCalendar(2018,0,25)
                ),
                equalTo(-1)
        );
    }

    @Test
    public void getDateDiff_difference_sameday() throws Exception{

        //mock Log.v
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,11)))).thenReturn(0);
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,10)))).thenReturn(0);
        assertThat(
                "Current Date is same day",
                LPG_Utility.getDateDiff(
                        new GregorianCalendar(2018,0,26),
                        new GregorianCalendar(2018,0,26)
                ),
                equalTo(0)
        );
    }

    @Test
    public void getDateDiff_difference_nextday() throws Exception{

        //mock Log.v
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,11)))).thenReturn(0);
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,10)))).thenReturn(0);
        assertThat(
                "Current Date is the very next day",
                LPG_Utility.getDateDiff(
                        new GregorianCalendar(2018,0,26),
                        new GregorianCalendar(2018,0,27)
                ),
                equalTo(1)
        );
    }

    @Test
    public void getDateDiff_difference_5Dec18() throws Exception{

        //mock Log.v
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,11)))).thenReturn(0);
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,10)))).thenReturn(0);
        assertThat(
                "Current Date is 5 Dec 18",
                LPG_Utility.getDateDiff(
                        new GregorianCalendar(2018,0,26),
                        new GregorianCalendar(2018,11,5)
                ),
                equalTo(313)
        );
    }

    @Test
    public void getDateDiff_difference_8Mar18() throws Exception{

        //mock Log.v
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,11)))).thenReturn(0);
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,10)))).thenReturn(0);
        assertThat(
                "Current Date is 8 Mar 18",
                LPG_Utility.getDateDiff(
                        new GregorianCalendar(2018,0,26),
                        new GregorianCalendar(2018,2,8)
                ),
                equalTo(41)
        );
    }

    @Test
    public void getDateDiff_difference_27Jan19() throws Exception{

        //mock Log.v
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,11)))).thenReturn(0);
//        when(mLog.v("DateDiff", " Earlier Date " + LPG_Utility.getStringDate(new GregorianCalendar(2018,0,10)))).thenReturn(0);
        assertThat(
                "Current Date is 27 Jan 19",
                LPG_Utility.getDateDiff(
                        new GregorianCalendar(2018,0,26),
                        new GregorianCalendar(2019,0,27)
                ),
                equalTo(366)
        );
    }

}