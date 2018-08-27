package k.agera.com.locationwidget.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import k.agera.com.locationwidget.R
import k.agera.com.locationwidget.location.PositionActivity

class MainActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.btn_location)?.setOnClickListener {
            startActivity(Intent(MainActivity@ this, PositionActivity::class.java))
        }

    }
}
