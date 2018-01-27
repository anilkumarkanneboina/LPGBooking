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

    @Test
    public void getExpiryStatus_08Jan18_and_45(){
        assertEquals("Last booked date is 8 Jan 18 and expiry Days is 45",
                (float) LPG_Utility.getExpiryStatus("08/1/2018",45),
                42.0,
                0.0
        );

    }

    @Test
    public void getExpiryStatus_27Jan18_and_10(){
        assertEquals("Last booked date is 27 Jan 18 and expiry Days is 10",
                (float) LPG_Utility.getExpiryStatus("27/1/2018",10),
                0.0,
                0.0
        );

    }

    @Test
    public void getExpiryStatus_26Jan18_and_2(){
        assertEquals("Last booked date is 26 Jan 18 and expiry Days is 2",
                (float) LPG_Utility.getExpiryStatus("26/1/2018",2),
                50.0,
                0.0
        );

    }

    @Test
    public void getExpiryStatus_02Jan18_and_30(){
        assertEquals("Last booked date is 2 Jan 18 and expiry Days is 30",
                (float) LPG_Utility.getExpiryStatus("02/1/2018",30),
                83.0,
                0.0
        );

    }

    @Test
    public void getExpiryStatus_22Jan18_and_10(){
        assertEquals("Last booked date is 22 Jan 18 and expiry Days is 10",
                (float) LPG_Utility.getExpiryStatus("22/1/2018",10),
                50.0,
                0.0
        );

    }

    @Test
    public void getExpiryStatus_02Jan18_and_45(){
        assertEquals("Last booked date is 2 Jan 18 and expiry Days is 45",
                (float) LPG_Utility.getExpiryStatus("02/1/2018",45),
                55.0,
                0.0
        );

    }

    @Test
    public void getExpiryStatus_10Jan18_and_45(){
        assertEquals("Last booked date is 10 Jan 18 and expiry Days is 45",
                (float) LPG_Utility.getExpiryStatus("10/1/2018",45),
                37.0,
                0.0
        );

    }

    @Test
    public void getExpiryStatus_31Dec17_and_45(){
        assertEquals("Last booked date is 8 Jan 18 and expiry Days is 45",
                (float) LPG_Utility.getExpiryStatus("31/12/2017",45),
                60.0,
                0.0
        );

    }










}