package com.telkomschools.smart_reply_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var conversation = ArrayList<FirebaseTextMessage>()
    private lateinit var inputName: EditText
    private lateinit var hint0: Button
    private lateinit var hint1: Button
    private lateinit var hint2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputName = findViewById(R.id.name)
        hint0 = findViewById(R.id.suggest1)
        hint1 = findViewById(R.id.suggest2)
        hint2 = findViewById(R.id.suggest3)

        val btnSend: Button = findViewById(R.id.send)
        val btnHint: Button = findViewById(R.id.hint)
        val btnClear: Button = findViewById(R.id.clear)

        btnSend.setOnClickListener {
            addMessage(pesan.text.toString())
        }

        btnHint.setOnClickListener {
            getHint()
        }

        btnClear.setOnClickListener {
            conversation = ArrayList()

            hint0.visibility = View.GONE
            hint1.visibility = View.GONE
            hint2.visibility = View.GONE
            inputName.error = ""
        }
    }

    private fun addMessage(text: String){
        conversation.add(
            FirebaseTextMessage.createForRemoteUser(
                text, System.currentTimeMillis(), inputName.text.toString()
            )
        )
    }

    private fun getHint(){
        val smartReply = FirebaseNaturalLanguage.getInstance().smartReply
        smartReply.suggestReplies(conversation)
            .addOnSuccessListener{result ->
                if (result.status == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE){
                    inputName.error = "Bahasa tidak disupport"
                }else if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS){
                    hint0.text = result.suggestions[0].text
                    hint1.text = result.suggestions[1].text
                    hint2.text = result.suggestions[2].text

                    hint0.visibility = View.VISIBLE
                    hint1.visibility = View.VISIBLE
                    hint2.visibility = View.VISIBLE
                    inputName.error = ""
                }
            }
            .addOnFailureListener{
                inputName.error = it.toString()
            }
    }
}
