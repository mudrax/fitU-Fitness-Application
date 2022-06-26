package com.mudrax.fitu

class ExerciseModel(private var id:Int ,private var name: String ,private var image: Int ,private var isSelected: Boolean = false ,private var isCurrent:Boolean = false) {


    fun getId():Int{
        return id
    }
    fun setId(id:Int)
    {
        this.id = id
    }

    fun getName():String{
        return name
    }
    fun setName(name:String)
    {
        this.name = name
    }

    fun getImage():Int{
        return image
    }
    fun setImage(image:Int)
    {
        this.image = image
    }

    fun getIsSelected():Boolean{
        return isSelected
    }
    fun setIsSelected(isSelected:Boolean)
    {
        this.isSelected = isSelected
    }

    fun getIsCurrent():Boolean{
        return isCurrent
    }
    fun setIsSCurrent(isCurrent:Boolean)
    {
        this.isCurrent = isCurrent
    }
}