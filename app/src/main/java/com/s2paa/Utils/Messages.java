package com.s2paa.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 7/4/2017.
 */

public class Messages {

    public static void ShowMessage(Context context,String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
