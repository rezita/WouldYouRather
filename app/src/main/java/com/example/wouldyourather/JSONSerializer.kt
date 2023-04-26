package com.example.wouldyourather

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONTokener
import java.io.*

class JSONSerializer(private val fileName: String,
                     private val context: Context) {

    @Throws(IOException::class, JSONException::class)
    fun save(questions: List<Question>){
        val jsonArray = JSONArray()
        for (question in questions){
            jsonArray.put(question.convertToJSON())
        }

        var writer: Writer? = null
        try {
            val out = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            writer = OutputStreamWriter(out)
            writer.write(jsonArray.toString())
        } finally {
            writer?.close()
        }
    }

    @Throws(IOException::class, JSONException::class)
    fun load():ArrayList<Question>{
        val questions = ArrayList<Question>()
        var reader : BufferedReader? = null
        try {
            val `in` = context.openFileInput(fileName)
            reader = BufferedReader(InputStreamReader(`in`))
            val jsonString = StringBuilder()

            for (line in reader.readLine()) {
                jsonString.append(line)
            }

            val jsonArray = JSONTokener(jsonString.toString()).nextValue() as JSONArray

            for (i in 0 until jsonArray.length()) {
                questions.add(Question(jsonArray.getJSONObject(i)))
            }
        } catch (e:FileNotFoundException){
            Log.i("info", "There is no JSON file.")
        } finally {
            //it throws an error if the reader is null - questions will be an empty list
            reader!!.close()
        }
        return questions
    }
}