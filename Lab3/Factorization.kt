package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Factorization : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factorization)
        val calc = findViewById<View>(R.id.Calc) as Button
        calc.setOnClickListener {
            val number = findViewById<View>(R.id.number) as TextView
            val res = findViewById<View>(R.id.res) as TextView
            res.text = fact(number.text.toString().toInt())
        }
    }

    fun fact(n: Int): String {
        var s: Int
        var y = 0.0
        var res = ""
        s = Math.ceil(Math.sqrt(n.toDouble())).toInt()
        while (true) {
            y = Math.sqrt(Math.pow(s.toDouble(), 2.0) - n)
            if (y % 1 == 0.0) {
                res = """
                    x=${(s - y).toInt()}
                    y=${(s + y).toInt()}
                    """.trimIndent()
                return res
            }
            s++
        }
    }

    override fun onBackPressed() {
        try {
            val intent = Intent(this@Factorization, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
        }
    }
}