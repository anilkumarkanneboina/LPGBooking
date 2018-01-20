package com.aekan.navya.lpgbooking.purchase;

import com.android.billingclient.api.PurchasesUpdatedListener;

/**
 * Created by arunramamurthy on 01/01/18.
 */

public interface BillingManager  {


    public void getSKUDetails(String SKUID);
    public int isSKUPurchased(String SKUID);
    public boolean isReady();
    public void closeConnection();



}
