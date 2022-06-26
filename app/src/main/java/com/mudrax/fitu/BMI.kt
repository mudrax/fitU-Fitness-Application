package com.mudrax.fitu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mudrax.fitu.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class BMI : AppCompatActivity() {
    private var binding: ActivityBmiBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //step 1
        setSupportActionBar(binding?.tbBmi)//layout starts supporting toolbar
        //making a back button

        if(supportActionBar!=null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }//step 2 of back button

        binding?.tbBmi?.setNavigationOnClickListener{
            onBackPressed()
        }//step 1 : what function back button will do

        binding?.btnCalculate?.setOnClickListener{
            if(binding?.etHeight?.text!!.isNotEmpty() && binding?.etWeight?.text!!.isNotEmpty())
            {
                val sheight = binding?.etHeight?.text.toString()
                val height = sheight.toDouble()

                val sweight = binding?.etWeight?.text.toString()
                val weight = sweight.toDouble()

                var bmi = bmiCalculation(height , weight)
                val bd= BigDecimal(bmi)
                val num = bd.setScale(2,RoundingMode.FLOOR)
                bmi = num.toDouble()

                binding?.tvYourBmi?.visibility = View.VISIBLE
                binding?.tvBmiCalculated?.visibility = View.VISIBLE
                binding?.tvBmiCalculated?.text= bmi.toString()

                makeVisibleChangesInBmiDescription(bmi.toString())


            }
            else
            {
                Toast.makeText(this, "height and weight cant be empty", Toast.LENGTH_SHORT).show()
            }
        }








    }

    fun makeVisibleChangesInBmiDescription(bmi :String)
    {
        val numBMI = bmi.toDouble()
        when{
            (numBMI>0 && numBMI<18.5)->{
                binding?.tvBmiDesc?.text = "Underweight 😞 You should focus on maintaining more Mass"
            }
            (numBMI in 18.5..24.9)->{

                binding?.tvBmiDesc?.text = "Normal 😃 !! Congratulations,Now the key is to maintain the Body"
            }
            (numBMI in 25.0..29.9)->{

                binding?.tvBmiDesc?.text = "Overweight, Work hard and eat Good and most importantly Be Active ☺"
            }
            (numBMI in 30.0..34.5)->{

                binding?.tvBmiDesc?.text = "Obese  😣 Exercise hard and Controlling the Diet is Key!  "
            }
            (numBMI >=35)->{

                binding?.tvBmiDesc?.text = "Extremely Obese.. This is THE RED ALERT. But dont worry this app will help you getting Lean 😉"
            }

        }
        binding?.tvBmiDesc?.visibility = View.VISIBLE

    }

    private fun bmiCalculation ( height:Double ,  weight:Double):Double
    {
        val bmi = (weight*(10000)) / (height*height)
        return bmi

    }

//    fun resetData()
//    {
//        binding?.etHeight?.setHint(null)
//        binding?.etWeight?.setHint(null)
//    }
//
//    fun metricSystemBMICalculation()
//    {
//        binding?.tvBmiDesc?.visibility = View.VISIBLE
//        binding?.tvBmiCalculated?.visibility = View.VISIBLE
//        binding?.tvYourBmi?.visibility = View.VISIBLE
//
//
//        val stringHeight = binding?.etHeight?.text.toString()
//        val floatHeight = (stringHeight.toFloat())/100
//
//        val stringWeight = binding?.etWeight?.text.toString()
//        val floatWeight = stringWeight.toFloat()
//
//        val bmiCalculation = floatWeight / (floatHeight*floatHeight)
//
//        binding?.tvBmiCalculated?.text = bmiCalculation.toString()
//
//        when{
//            (bmiCalculation>0 && bmiCalculation<18.5)->{
//                binding?.tvBmiDesc?.text = "Underweight 😞 You should focus on maintaining more Mass"
//            }
//            (bmiCalculation in 18.5..24.9)->{
//
//                binding?.tvBmiDesc?.text = "Normal 😃 !! Congratulations,Now the key is to maintain the Body"
//            }
//            (bmiCalculation in 25.0..29.9)->{
//
//                binding?.tvBmiDesc?.text = "Overweight, Work hard and eat Good and most importantly Be Active ☺"
//            }
//            (bmiCalculation in 30.0..34.5)->{
//
//                binding?.tvBmiDesc?.text = "Obese  😣 Exercise hard and Controlling the Diet is Key!  "
//            }
//            (bmiCalculation >=35)->{
//
//                binding?.tvBmiDesc?.text = "Extremely Obese.. This is THE RED ALERT. But dont worry this app will help you getting Lean 😉"
//            }
//
//        }
//    }
//
////    fun usSystemBMICalculation()
////    {
////        binding?.tvBmiDesc?.visibility = View.VISIBLE
////        binding?.tvBmiCalculated?.visibility = View.VISIBLE
////        binding?.tvYourBmi?.visibility = View.VISIBLE
////
////        val stringHeight = binding?.etHeight?.text.toString()
////        val floatHeight = (stringHeight.toFloat())/100
////
////        val stringWeight = binding?.etWeight?.text.toString()
////        val floatWeight = stringWeight.toFloat()
////
////
////        val bmiCalculation = (floatWeight / (floatHeight*floatHeight))*703
////
////        binding?.tvBmiCalculated?.text = bmiCalculation.toString()
////
////        when{
////            (bmiCalculation>0 && bmiCalculation<18.5)->{
////                binding?.tvBmiDesc?.text = "Underweight 😞 You should focus on maintaining more Mass"
////            }
////            (bmiCalculation in 18.5..24.9)->{
////
////                binding?.tvBmiDesc?.text = "Normal 😃 !! Congratulations,Now the key is to maintain the Body"
////            }
////            (bmiCalculation in 25.0..29.9)->{
////
////                binding?.tvBmiDesc?.text = "Overweight, Work hard and eat Good and most importantly Be Active ☺"
////            }
////            (bmiCalculation in 30.0..34.5)->{
////
////                binding?.tvBmiDesc?.text = "Obese  😣 Exercise hard and Controlling the Diet is Key!  "
////            }
////            (bmiCalculation >=35)->{
////
////                binding?.tvBmiDesc?.text = "Extremely Obese.. This is THE RED ALERT. But dont worry this app will help you getting Lean 😉"
////            }
////
////        }
////    }
//


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}