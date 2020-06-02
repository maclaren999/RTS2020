package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_factorization.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
/*

Реализовал отмену операции по таймауту в 1 секунду через асинхронные таски.
Скоро сделаю pop-up.

*/
class Factorization : AppCompatActivity() {

    val JOB_TIMEOUT: Long = 1000L
    var inputNum : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factorization)

    }

    //  При нажатии на кнопку
    fun onClickGo(v: View) {
        inputNum = try {
            number.text.toString().toInt()
        }catch (e: NumberFormatException){
            resFactView.text = "Вы ввели слишком большое число"
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            return
        }
        // Вызов билдера корутин
        CoroutineScope(Main).launch {
            try {
                resFactView.text = factExecAndSet()
            } catch (e: TimeoutCancellationException) {
                resFactView.text = "Выполнение прервано: длилось дольше 1 сек"
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun factExecAndSet(): String {
        return withContext(IO) {
//           Корутина с таймером
            withTimeout(JOB_TIMEOUT) {
                fact(inputNum)
            }
        }
    }

    suspend fun fact(n: Int): String {
        var s: Int
        var y = 0.0
        val result: String
        s = Math.ceil(Math.sqrt(n.toDouble())).toInt()
        while (true) {
            y = Math.sqrt(Math.pow(s.toDouble(), 2.0) - n)
            if (y % 1 == 0.0) {
                result = """
                    x=${(s - y).toInt()}
                    y=${(s + y).toInt()}
                    """.trimIndent()
                return result
            }
            s++
            yield() //Приостановка ради возможности выбросить исключение
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@Factorization, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
