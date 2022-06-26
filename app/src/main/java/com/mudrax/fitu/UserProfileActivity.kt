package com.mudrax.fitu

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.mudrax.fitu.Constants.USER_NAME
import com.mudrax.fitu.Constants.emailID_of_USER
import com.mudrax.fitu.databinding.ActivityFeedbackBinding
import com.mudrax.fitu.databinding.ActivityUserProfileBinding
import java.text.SimpleDateFormat
import java.util.*

class UserProfileActivity : AppCompatActivity() {

    private var binding: ActivityUserProfileBinding?=null
    lateinit var toggle: ActionBarDrawerToggle // lateinitvar promises that we will put some value in this variable in future but now let it empty

    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var fireStore = FirebaseFirestore.getInstance()
        fillDetails(getUserFirebaseId())

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        toggle = ActionBarDrawerToggle(this , binding?.drawerLayout, R.string.open ,R.string.close)
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // converts drawer to backButton when drawer is opened

        //how to respond to menu item clicks
        binding?.navView?.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.menuUserProfile-> {
                    Toast.makeText(applicationContext, "You are already here", Toast.LENGTH_SHORT).show()

                }
                R.id.menuExercise-> {
                    Toast.makeText(applicationContext, "menu exercise 1", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this , ExerciseActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuNews-> {
                    val intent = Intent(this , NewsActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuShop-> {
                    val intent = Intent(this , ShopActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.menuHome-> {
                    val intent = Intent(this , HomePage::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.menuProfile-> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this , MainActivity::class.java)
                    startActivity(intent)
                    finish()


                }
                R.id.menuFeedback->{
                    val intent = Intent(this , FeedbackActivity::class.java)
                    startActivity(intent)
                }
            }
            true // since this is a lambda expression return true means we handled clicks
        }

        binding?.btnUser?.setOnClickListener {
            //Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show()
            if(binding?.etName?.text.toString().isNotEmpty() && binding?.etAge?.text.toString().isNotEmpty() && binding?.etEmail?.text.toString().isNotEmpty() && binding?.etPhone?.text.toString().isNotEmpty())
            {

                USER_NAME = binding?.etName?.text.toString()
                emailID_of_USER = binding?.etEmail?.text.toString()


                val userProfileDataFormat = UserProfileDataFormat(binding?.etName?.text.toString(),
                    binding?.etAge?.text.toString() ,
                    binding?.etEmail?.text.toString() ,
                    binding?.etPhone?.text.toString()
                )


                fireStore = FirebaseFirestore.getInstance()

                uploadImage()
                fireStore.collection("Users").document(getUserFirebaseId()).set(userProfileDataFormat , SetOptions.merge())
                    .addOnSuccessListener {

                        Toast.makeText(this, "Succesfully updated to database!", Toast.LENGTH_SHORT)
                            .show()

                        binding?.etName?.text?.clear()
                        binding?.etAge?.text?.clear()
                        binding?.etEmail?.text?.clear()
                        binding?.etPhone?.text?.clear()
                        binding?.btnUser?.setTextSize(32.toFloat())
                        binding?.btnUser?.text = "GOTO HOMEPAGE"
                        binding?.btnUser?.setOnClickListener {
                            val intent = Intent(this, HomePage::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener {
                        //Toast.makeText(this, "failed entry", Toast.LENGTH_SHORT).show()

                    }

            }
            else
            {
                Toast.makeText(this, "Fields cant be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding?.ivUser?.setOnClickListener{
            selectImage()
        }
    }

    private fun fillDetails(id:String) {
        val database = FirebaseFirestore.getInstance()
        database.collection("Users").document(id).get()
            .addOnSuccessListener {
                if(it.exists())
                {
                    val fetchedName:String = it.data?.getValue("name").toString()
                    val fetchAge = it.data?.getValue("age").toString()
                    val fetchEmail = it.data?.getValue("email").toString()
                    val fetchPhone = it.data?.getValue("phone").toString()

                    binding?.etName?.setText(fetchedName)
                    binding?.etAge?.setText(fetchAge)
                    binding?.etEmail?.setText(fetchEmail)
                    binding?.etPhone?.setText(fetchPhone)
                }
                else
                {
                    Toast.makeText(this, "ID found but problem persists!!!", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, "Unable to fetch the data", Toast.LENGTH_SHORT).show()
            }

    }

    private fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==100 && resultCode== RESULT_OK)
        {
            imageUri = data?.data!!
            binding?.ivUser?.setImageURI(imageUri)
        }
    }

    private fun uploadImage(){

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File...")
        progressDialog.setCancelable(false)
        progressDialog.show()


        val fileName = getUserFirebaseId()
        val storageReference = FirebaseStorage.getInstance().getReference(fileName)



        storageReference.putFile(imageUri).
                addOnSuccessListener {
                    binding?.ivUser?.setImageURI(null)
                    Toast.makeText(this@UserProfileActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                    if(progressDialog.isShowing)progressDialog.dismiss()
                }.
                addOnFailureListener{

                    if(progressDialog.isShowing)progressDialog.dismiss()
                    //Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }

    }

    private fun getUserFirebaseId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}