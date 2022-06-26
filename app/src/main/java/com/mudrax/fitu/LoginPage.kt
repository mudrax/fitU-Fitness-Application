package com.mudrax.fitu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mudrax.fitu.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {
    private var binding: ActivityLoginPageBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.signUp?.setOnClickListener{
            binding?.signIn?.text = "Sign Up"
            binding?.signUp?.background = resources.getDrawable(R.drawable.switch_trcks)
            binding?.signUp?.setTextColor(resources.getColor((R.color.textColor)))
            binding?.logIn?.background = null
            binding?.signUpLayout?.visibility = View.VISIBLE
            binding?.logInLayout?.visibility = View.GONE
            binding?.logIn?.setTextColor(resources.getColor(R.color.pinkColor))
        }
        binding?.logIn?.setOnClickListener{
            binding?.signIn?.text = "Log In"
            binding?.signUp?.background = null
            binding?.logIn?.background = resources.getDrawable(R.drawable.switch_trcks)
            binding?.logIn?.setTextColor(resources.getColor((R.color.textColor)))
            binding?.signUpLayout?.visibility = View.GONE
            binding?.logInLayout?.visibility = View.VISIBLE
            binding?.signUp?.setTextColor(resources.getColor(R.color.pinkColor))
        }





    }
}