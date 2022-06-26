package com.mudrax.fitu

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.SyncStateContract
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mudrax.fitu.databinding.ActivityExerciseBinding
import com.mudrax.fitu.databinding.DialogueBoxBackpressBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding?=null
    private var doNotStartFinishActivity = false

    private var tts:TextToSpeech?=null

    private var player: MediaPlayer?=null

    private var exerciseAdapter: ExerciseAdapter?=null

    private var restTimer: CountDownTimer?=null//counts from 10 to 0
    private var restProgress  = 0 //how far we have rested
    private var exerciseTimer:CountDownTimer?=null
    private var exerciseProgress = 0

    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var exerciseCurrentPosition:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //setting doNotStartFinishActivity to false
        doNotStartFinishActivity = false

        //Toolbar steps - > goto xml and finalise xml frontend
        tts = TextToSpeech(this , this)

        //step 1
        setSupportActionBar(binding?.toolbarExercise)//layout starts supporting toolbar
        //making a back button

        if(supportActionBar!=null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "WORKOUT APP"
        }//step 2 of back button

        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogueBoxPress()
        }//step 1 : what function back button will do


        exerciseList = Constants.defaultExerciseList()
        setUpRestTimerFun()
        setUpRecyclerView()


    }

    override fun onBackPressed() {
        customDialogueBoxPress()
    }

    private fun customDialogueBoxPress()
    {
        val customDialog  = Dialog(this)
        val dialogBinding = DialogueBoxBackpressBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false) // bahar touch kiya hai dilogue box ke , toh box band na ho

        dialogBinding.btnYes.setOnClickListener{
            doNotStartFinishActivity = true
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }

        customDialog.show()

    }

    private fun setUpRecyclerView()
    {
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setUpRestTimerFun()
    {
        try {
            val soundURI = Uri.parse("android.resource://com.mudrax.new7minuteworkout/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext , soundURI)
            player?.isLooping = false
            player?.start()
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }

        binding?.flProgressBar?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.flExerciseProgressBar?.visibility = View.INVISIBLE

        if(restTimer!=null)
        {
            restTimer?.cancel()
            //////////////////////////
            restProgress = 0
        }

        speakUp("REST FOR ten seconds")

        setRestTimer()
    }
    private fun setUpExerciseTimerFun()
    {
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseProgressBar?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE



        if(exerciseTimer!=null)
        {
            exerciseTimer?.cancel()
            ///////////////////////////////////////////
            exerciseProgress = 0
        }

        speakUp(exerciseList!![exerciseCurrentPosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![exerciseCurrentPosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![exerciseCurrentPosition].getName()


        setExerciseTimerFun()
    }

    private fun setRestTimer()
    {
        binding?.progressBar?.progress = restProgress
        binding?.tvUpcomingExerciseName?.text = exerciseList!![exerciseCurrentPosition+1].getName()
        restTimer = object : CountDownTimer(1000 , 1000)
        {
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = 10-restProgress
                binding?.tvTimer?.text = (10-restProgress).toString()


            }

            override fun onFinish() {
                exerciseCurrentPosition++

                exerciseList!![exerciseCurrentPosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setUpExerciseTimerFun()
            }

        }.start()
    }

    private fun setExerciseTimerFun()
    {
        binding?.exerciseProgressBar?.progress = exerciseProgress
        exerciseTimer = object:CountDownTimer(3000 , 1000)
        {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.exerciseProgressBar?.progress = 30- exerciseProgress
                binding?.tvTimerExercise?.text = (30-exerciseProgress).toString()
            }

            override fun onFinish() {





                if(exerciseCurrentPosition<exerciseList!!.size-1)
                {
                    exerciseList!![exerciseCurrentPosition].setIsSelected(false)
                    exerciseList!![exerciseCurrentPosition].setIsSCurrent(true)
                    exerciseAdapter!!.notifyDataSetChanged()

                    setUpRestTimerFun()
                }
                else
                {
                    if(doNotStartFinishActivity==false)
                    {
                        val intent = Intent(this@ExerciseActivity , ResultActivity::class.java)
                        startActivity(intent)
                        finish()
                    }



                }
            }

        }.start()
    }

    override fun onDestroy() {

        if(player!=null)
        {
            player?.stop()
        }

        if(restTimer!=null)
        {
            restTimer?.cancel()
            //////////////////////////////////
            restProgress = 0
        }

        if(tts!=null)
        {
            tts?.stop()
            tts?.shutdown()
        }

        super.onDestroy()
        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS)
        {
            val result = tts?.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
            {
                Toast.makeText(this, "NOT SUPPORTED CONTENT", Toast.LENGTH_SHORT).show()
            }

        }
        else
        {
            Toast.makeText(this, "NOT SUCCESS (inside the else)", Toast.LENGTH_SHORT).show()
        }

    }

    private fun speakUp(text:String)
    {
        tts?.speak(text , TextToSpeech.QUEUE_FLUSH , null , "")
    }


}