package com.aekan.navya.lpgbooking.purchase;

import com.android.billingclient.api.SkuDetails;

import java.util.List;

/**
 * Created by arunramamurthy on 19/01/18.
 */

public interface BillingConsumer {
    public void updateSKUInfo(List<SkuDetails> skuDetails);
    public void updatePurchaseInfo(boolean premiumUserStatus);
}
