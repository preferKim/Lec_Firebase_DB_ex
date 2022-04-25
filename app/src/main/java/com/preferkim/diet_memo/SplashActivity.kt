package com.preferkim.diet_memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class SplashActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance() // auth = Firebase.auth 대체

        try {

            Log.d("splash", firebaseAuth.currentUser!!.uid)

            Toast.makeText(
                this, "원래 비회원 로그인 되어있는 사람입니다.",
                Toast.LENGTH_SHORT
            ).show()

            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3000)

        } catch (e: Exception) {

            Log.d("splash", "회원가입 시켜줘야함")

            firebaseAuth.signInAnonymously()
                .addOnSuccessListener {
                    Toast.makeText(
                        this, "익명 로그인 성공",
                        Toast.LENGTH_SHORT
                    ).show()

                    Handler().postDelayed({
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }, 3000)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this, "익명 로그인 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }



    }
}