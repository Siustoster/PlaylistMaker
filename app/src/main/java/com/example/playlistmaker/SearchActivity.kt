package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged

class SearchActivity : AppCompatActivity() {
    private var editTextString: String = EDIT_TEXT_DEF


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_NAME, editTextString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextString = savedInstanceState.getString(EDIT_TEXT_NAME, EDIT_TEXT_DEF)
        val editText = findViewById<EditText>(R.id.search_edit_text)
        editText.setText(editTextString)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val clearButton = findViewById<ImageView>(R.id.clear_search_button)
        val editText = findViewById<EditText>(R.id.search_edit_text)
        val backButton = findViewById<ImageButton>(R.id.search_back_button)

        backButton.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            editText.text.clear()
            editText.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                editText.applicationWindowToken,
                0
            ) //что бы скрыть клавиатуру по тз. Просто очистка фокуса не помогала.
        }
        editText.doAfterTextChanged { s ->
            editTextString = s.toString()
            if (s.isNullOrEmpty()) {
                clearButton.visibility = View.INVISIBLE
            } else clearButton.visibility = View.VISIBLE
        }
        //editText.addTextChangedListener(simpleTextWatcher)
    }

    companion object {
        const val EDIT_TEXT_NAME = "SEARCH_EDIT_TEXT"
        const val EDIT_TEXT_DEF = ""
    }
}
