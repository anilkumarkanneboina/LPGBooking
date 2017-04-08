package com.aekan.navya.lpgbooking.utilities;

import android.os.Messenger;

/**
 * Created by aruramam on 4/8/2017.
 * Promise interface with methods defined to service client API request.
 * Helps as a wrapper on implementation logic in HandlerThread mechanism.
 */

public interface LPGServiceAPI {

    public void populateCylinderInfoThroughCursorWithRowID(String rowID, Messenger messenger);

}
