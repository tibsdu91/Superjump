package com.doodlejump

import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity(), OnTouchListener {

    var playButton = RectF(16f, 324f, 272f, 464f)
    private lateinit var menuView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        menuView  = findViewById(R.id.menu)
        menuView.setOnTouchListener(this)
    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        if(event == null) return false;
        if(event.action == MotionEvent.ACTION_DOWN && playButton.contains(event.x / menuView.width * 486, event.y / menuView.height * 864)) {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)

        }
        return true;
    }

}