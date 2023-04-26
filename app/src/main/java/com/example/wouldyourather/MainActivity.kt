package com.example.wouldyourather

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.wouldyourather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val questions: ArrayList<Question> = ArrayList()
    private var index: Int = 0
    private var qsSerializer: JSONSerializer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        qsSerializer = JSONSerializer(
            resources.getString(R.string.json_filename),
            applicationContext
        )

        init()
        showQuestion()
    }

    override fun onPause() {
        saveQuestions()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val isNotEmptyQuestions = (questions.size != 0)
        menu?.findItem(R.id.menu_delete)?.setVisible(isNotEmptyQuestions)
        menu?.findItem(R.id.menu_edit)?.setVisible(isNotEmptyQuestions)
        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isNotEmptyQuestions = (questions.size != 0)
        menu?.findItem(R.id.menu_delete)?.setVisible(isNotEmptyQuestions)
        menu?.findItem(R.id.menu_edit)?.setVisible(isNotEmptyQuestions)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_new -> {
                createNewQuestion()
                true
            }
            R.id.menu_delete -> {
                removeQuestion()
                true
            }
            R.id.menu_edit -> {
                editCurrentQuestion()
                true
            }
            R.id.menu_backup -> {
                reloadOriginalQuestions()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isFirstTimeUser(): Boolean {
        val preferences = this.getPreferences(Context.MODE_PRIVATE)
        return preferences.getBoolean(R.string.first_time_use.toString(), true)
    }

    private fun setFirstTimeUser() {
        val preferences = this.getPreferences(Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(R.string.first_time_use.toString(), false)
        editor.apply()
    }

    private fun init() {
        //preloadQuestions()
        if (isFirstTimeUser()){
            //load questions from raw resource
            preloadQuestions()
            setFirstTimeUser()
        } else {
            //load from JSON
            try {
                questions.clear()
                questions.addAll(qsSerializer!!.load())
            } catch (e: Exception) {
                Log.e("Error loading questions: ", "", e)
                Toast.makeText(this,"Cannot load saved questions.", Toast.LENGTH_SHORT).show()
            }
            if (questions.isEmpty()){
                preloadQuestions()
            }
        }
        binding.btnNextQuestion.setOnClickListener { showQuestion() }
    }

    private fun preloadQuestions() {
        Log.i("info", "preload questions")
        val inputStream = applicationContext.resources.openRawResource(R.raw.question_preload)
        val buffer = inputStream.bufferedReader()
        val lines = buffer.readLines()
        for (line in lines) {
            val tokens = line.split("£$")
            val question = Question(tokens[0], tokens[1])
            questions.add(question)
        }
    }

    @SuppressLint("ResourceType")
    private fun showQuestion() {
        invalidateOptionsMenu()
        if (questions.size > 0) {
            index = (0 until questions.size).random()
            showQuestion(questions[index])

        } else {
            binding.noQuestionText.visibility = View.VISIBLE
            binding.layoutWholeQuestion.visibility = View.GONE
        }
    }

    private fun showQuestion(question: Question) {
        binding.layoutWholeQuestion.visibility = View.VISIBLE
        binding.showOption1.text = question.option1
        binding.showOption2.text = question.option2
        binding.noQuestionText.visibility = View.GONE
    }

    private fun saveQuestions() {
        try {
            qsSerializer!!.save(questions)
        } catch (e: Exception) {
            Log.e("Error Saving Questions", "", e)
        }
    }

    fun addNewQuestion(newQuestion: Question) {
        questions.add(newQuestion)
        index = questions.indexOf(newQuestion)
        showQuestion(newQuestion)
    }

    fun deleteQuestion() {
        questions.removeAt(index)
        showQuestion()
    }

    private fun createNewQuestion() {
        val dialog = DialogNewQuestion()
        dialog.show(supportFragmentManager, "DialogNewQuestion")
    }

    private fun removeQuestion() {
        val dialog = DialogConfirm()
        dialog.show(supportFragmentManager, "DialogConfirm")
    }

    private fun editCurrentQuestion() {
        val dialog = DialogEditQuestion()
        dialog.show(supportFragmentManager, "DialogEditQuestion")
    }

    fun getCurrentQuestion(): Question{
        return questions[index]
    }

    fun editQuestion(op1: String, op2: String){
        questions[index].amend(op1, op2)
        showQuestion(questions[index])
    }

    private fun reloadOriginalQuestions(){
        questions.clear()
        preloadQuestions()
        showQuestion()
    }
}