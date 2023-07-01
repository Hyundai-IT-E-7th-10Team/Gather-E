package com.kosa.gather_e

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.kosa.gather_e.databinding.ActivityBottomNavigationVarBinding


class BottomNavigationVarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationVarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationVarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
        updateResult()

//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation_var)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_list, R.id.navigation_map, R.id.navigation_mypage
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        updateResult(true)
    }
    private fun initFirebase(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.d("gather","Token : : ${task.result}")
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false) {
//        resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런처") +
//                if (isNewIntent) {
//                    "(으)로 갱신했습니다."
//                } else {
//                    "(으)로 실행했습니다."
//                }
    }

}