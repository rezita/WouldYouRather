package com.example.wouldyourather

import org.json.JSONException
import org.json.JSONObject

class Question (){

    var option1: String? = null
    var option2: String? = null
    private val jsonOption1 = "Option1"
    private val jsonOption2 = "Option2"

    constructor(op1: String, op2: String): this(){
        this.option1 = op1
        this.option2 = op2
    }

    @Throws(JSONException::class)
    constructor(jsonObject : JSONObject) : this() {
        this.option1 = jsonObject.getString(jsonOption1)
        this.option2 = jsonObject.getString(jsonOption2)
    }

    @Throws(JSONException::class)
    fun convertToJSON() : JSONObject{
        val jsonObject = JSONObject()
        jsonObject.put(jsonOption1, option1)
        jsonObject.put(jsonOption2, option2)
        return jsonObject
    }

    fun amend(op1: String, op2: String) {
        option1 = op1
        option2 = op2
    }
}