package com.sdp.ecommerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.sdp.ecommerce.R
import com.sdp.ecommerce.repository.ProductRepository

class TempActivity : AppCompatActivity() {

    lateinit var repo: ProductRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        repo = ProductRepository(applicationContext)
        val button  = findViewById<Button>(R.id.add)
        val name = findViewById<EditText>(R.id.name)
        val dis = findViewById<EditText>(R.id.discription)
        button.setOnClickListener {
            repo.addDataToFirestore(name.text.toString(), dis.text.toString())
        }


    }
}