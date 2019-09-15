package ru.deinega.customswitch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchview.setOnSwitchChangeListener(object : OnSwitchChangedListener {
            override fun onSwitchChanged(isChecked: Boolean) {
                Log.e("!!onSwitchChanged", isChecked.toString())
            }
        })

    }
}
