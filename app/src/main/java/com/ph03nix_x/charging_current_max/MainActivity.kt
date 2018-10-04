package com.ph03nix_x.charging_current_max
import android.content.Context
import android.content.Intent
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
        } catch (e: java.io.IOException) {
            AlertDialog.Builder(this).setTitle(getString(R.string.error)).setMessage(getString(R.string.root)).setPositiveButton("OK")
            { dialog_, _ ->

                dialog_.dismiss()
                finish()
            }.show()
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
                Runtime.getRuntime().exec("su -c echo \"$save_\" > /sys/class/power_supply/battery/constant_charge_current_max")
                getSharedPreferences("preferences", Context.MODE_PRIVATE).edit().putInt("current", save_).apply()
                Toast.makeText(this, getString(R.string.success_save), Toast.LENGTH_LONG).show()
            }
            R.id.default_ ->
            {
                var prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                var save_ = prefs.getString("mA_default", null)!!.toInt() * 1000
                Runtime.getRuntime().exec("su -c echo \"$save_\" > /sys/class/power_supply/battery/constant_charge_current_max")
                Toast.makeText(this, getString(R.string.success_def), Toast.LENGTH_LONG).show()
                runOnUiThread { mA_text?.setText(save_.toString()) }
            }
            R.id.github -> startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/Ph03niX-X/ChargingCurrentMax")))
            R.id.telegram -> startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://t.me/Ph03niX_X")))
            R.id._4pda -> startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("http://4pda.ru/forum/index.php?showtopic=915686")))
            R.id.about -> {
            var version: String = this.packageManager.getPackageInfo(packageName, 0).versionName
            var build: Int = this.packageManager.getPackageInfo(packageName, 0).versionCode
            AlertDialog.Builder(this).setTitle(getString(R.string.about))
                    .setMessage(getString(R.string.about_message1) + version + getString(R.string.about_message2) + build)
                    .setPositiveButton("OK") {dialog, _ -> dialog.dismiss()}
                    .show()
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
        AlertDialog.Builder(this).setTitle(getString(R.string.error)).setMessage(getString(R.string.file_not_found)).setPositiveButton("OK")
        { dialog, _ ->
            dialog.dismiss()
            finish()
        }.show()
    }
    return text_
}
}