package com.mudrax.fitu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.mudrax.fitu.Constants.USER_NAME
import com.mudrax.fitu.Constants.emailID_of_USER
import com.mudrax.fitu.databinding.ActivityHomePageBinding
import com.mudrax.fitu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val REQ_ONE_TAP = 2
    private var showOneTapUI = true
    private lateinit var googleSignInClient:GoogleSignInClient



    private var binding: ActivityMainBinding?= null
    private lateinit var firebaseAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)



        installSplashScreen()



        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)



        binding?.googleImageView?.setOnClickListener{
            Toast.makeText(this, "BTN TOUCJHED", Toast.LENGTH_SHORT).show()
            googleSignIn(it)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)





        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()


        firebaseAuth = FirebaseAuth.getInstance()

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

//        binding?.signIn?.setOnClickListener{
//            val intent=Intent(this , HomePage::class.java)
//            startActivity(intent)
//
//        }

        binding?.signIn?.setOnClickListener {
            // setting up first signup

            if(binding?.signUpLayout?.visibility == View.VISIBLE)
            {
                val email = binding?.eMails?.text.toString()
                val pass = binding?.passwordss01?.text.toString()
                val confirmPass = binding?.passwords01?.text.toString()

                if(email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty())
                {
                    if(pass==confirmPass)
                    {

                        emailID_of_USER = email
                        println(emailID_of_USER)
                        println(USER_NAME)
                        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                            if(it.isSuccessful)
                            {
                                val intent = Intent(this , HomePage::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show()
                    }

                }
                else
                {
                    Toast.makeText(this , "Fields cant be empty" , Toast.LENGTH_SHORT).show()
                }

            }
            else
            {
                //Login page if email id stored
                val lemail = binding?.eMail?.text.toString()
                val lpass = binding?.passwords?.text.toString()

                if(lemail.isNotEmpty() && lpass.isNotEmpty())
                {

                    emailID_of_USER = lemail
                    firebaseAuth.signInWithEmailAndPassword(lemail,lpass).addOnCompleteListener{
                        if(it.isSuccessful)
                        {
                            val intent = Intent(this , HomePage::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                else
                {
                    Toast.makeText(this , "Fields cant be empty" , Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    fun googleSignIn(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser!=null)
        {
            val intent = Intent(this , HomePage::class.java)
            startActivity(intent)
            finish()
        }

    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    val intent = Intent(this , HomePage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this@MainActivity, "google SignIn failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e)
            }
        }
    }


}