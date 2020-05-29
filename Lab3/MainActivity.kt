package com.example.lab3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        factorization.setOnClickListener {
                val intent = Intent(this@MainActivity, Factorization::class.java)
                startActivity(intent)
                finish()
        }
        parceptron.setOnClickListener {
                val intent = Intent(this@MainActivity, Parceptron::class.java)
                startActivity(intent)
                finish()
        }
        gen_alg.setOnClickListener {
                val intent = Intent(this@MainActivity, Genetic_alg::class.java)
                startActivity(intent)
                finish()
        }
    }
}