package com.ph03nix_x.charging_current_max

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context?.getSharedPreferences("preferences", Context.MODE_PRIVATE)?.getInt("current", 0).toString() !=
                context?.getSharedPreferences("preferences", Context.MODE_PRIVATE)?.getString("mA_default", null))
        {
try {
java.lang.Runtime.getRuntime().exec("su")
    java.lang.Runtime.getRuntime().exec("su -c echo \"${context?.getSharedPreferences("preferences", Context.MODE_PRIVATE)?.getInt("current", 0)}\" > /sys/class/power_supply/battery/constant_charge_current_max")
}
catch (e: java.io.IOException)
{
    Toast.makeText(context, "", Toast.LENGTH_LONG).show()
}
        }
    }
}