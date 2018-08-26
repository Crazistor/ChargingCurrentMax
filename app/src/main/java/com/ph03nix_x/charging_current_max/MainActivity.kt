package com.ph03nix_x.charging_current_max
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.*

class MainActivity : AppCompatActivity() {

    var mA_text: EditText? = null
    var mA: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mA_text = findViewById(R.id.editText)
        mA = findViewById(R.id.textView)
        mA_text?.setText(ReadFile())
        var prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        if (prefs.getString("mA_default", null) == null) {
            var editor = prefs.edit()
            editor.putString("mA_default", mA_text?.text.toString())
            editor.apply()
        }
        try {

            java.lang.Runtime.getRuntime().exec("su")
        } catch (e: Exception) {
            var dialog = AlertDialog.Builder(this).setTitle(getString(R.string.error)).setMessage(getString(R.string.root)).setPositiveButton("OK")
            { dialog_, _ ->

                dialog_.dismiss()
                finish()
            }
            dialog.show()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            R.id.save ->
            {
                var save_ = mA_text?.text.toString().toInt() * 1000
                Runtime.getRuntime().exec("su -c echo \"${save_}\" > /sys/class/power_supply/battery/constant_charge_current_max")
                Toast.makeText(this, getString(R.string.success_save), Toast.LENGTH_LONG).show()
            }
            R.id.default_ ->
            {
                var prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                var save_ = prefs.getString("mA_default", null).toInt() * 1000
                Runtime.getRuntime().exec("su -c echo \"${save_}\" > /sys/class/power_supply/battery/constant_charge_current_max")
                Toast.makeText(this, getString(R.string.success_def), Toast.LENGTH_LONG).show()
                runOnUiThread { mA_text?.setText(save_.toString()) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
fun ReadFile(): String?
{
    var text_: String? = null
    var i_ = 0
    try {
        var file = File("/sys/class/power_supply/battery/constant_charge_current_max")
        var input = FileInputStream(file)
        var isr = InputStreamReader(input)
        var buff = BufferedReader(isr)
        while (i_ != 1) {
                text_ = (buff.readLine().toInt() / 1000).toString()
                i_++
        }
        input.close()
    }
    catch (e: FileNotFoundException)
    {
var dialog = AlertDialog.Builder(this).setTitle(getString(R.string.error)).setMessage(getString(R.string.file_not_found)).setPositiveButton("OK")
{ dialog, _ ->
    dialog.dismiss()
    finish()
}
        dialog.show()
    }
    return text_
}
}