package com.mudrax.fitu

object Constants {

    var emailID_of_USER = "dummy@gmail.com"
    var USER_NAME = "dummy name"
    fun defaultExerciseList(): ArrayList<ExerciseModel>
    {
        val listOfExercises:ArrayList<ExerciseModel> = ArrayList<ExerciseModel>()

        val jumpingJacks = ExerciseModel(1, "Jumping Jacks" , R.drawable.gif_jumpingjack)
        val crunches = ExerciseModel(2,"Abdominal Crunches" , R.drawable.gif_crunches)
        val highKnees = ExerciseModel(3, "High Knees" , R.drawable.gif_high_knees)
        val lunges = ExerciseModel(4, "Lunges" , R.drawable.gif_lunges)
        val planks = ExerciseModel(5, "Planks" , R.drawable.gif_plank)
        val pushUp = ExerciseModel(6,"Push-Ups" , R.drawable.gif_pushup)
        val rotationPushUps = ExerciseModel(7 , "Rotational Push-ups" , R.drawable.gif_pushup_rotation)
        val sidePlanks = ExerciseModel(8,"Side Planks" , R.drawable.gif_side_planks)
        val squat = ExerciseModel(9 , "Squats" , R.drawable.gif_sqaut)
        val steps = ExerciseModel(10 , "Steps On Chair" , R.drawable.gif_chair_stand)
        val dips =ExerciseModel(11, "Triceps Dips" , R.drawable.gif_dips)
        val wallSit = ExerciseModel(12 , "Wall sit Hold" ,R.drawable.gif_wall_sit)

        listOfExercises.add(jumpingJacks)
        listOfExercises.add(crunches)
        listOfExercises.add(highKnees)
        listOfExercises.add(lunges)
        listOfExercises.add(planks)
        listOfExercises.add(pushUp)
        listOfExercises.add(rotationPushUps)
        listOfExercises.add(sidePlanks)
        listOfExercises.add(squat)
        listOfExercises.add(steps)
        listOfExercises.add(dips)
        listOfExercises.add(wallSit)

        return listOfExercises
    }
}