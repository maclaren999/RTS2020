package com.example.lab3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_factorization.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


/*

Pop-up - строка 93. Расчет времени в функции fact() - строка 85
Реализовал отмену операции по таймауту в 1 секунду через асинхронные таски.

*/
class Fact : AppCompatActivity() {

    val JOB_TIMEOUT: Long = 1000L
    var inputNum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factorization)

    }

    //  При нажатии на кнопку
    fun onClickGo(v: View) {
        inputNum = try {
            number.text.toString().toInt()
        } catch (e: NumberFormatException) {
            resFactView.text = "Вы ввели слишком большое число"
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            return
        }
        // Вызов билдера корутин
        var time = 0L
        CoroutineScope(Main).launch {
            try {
                /*(resFactView.text, time)*/
                var pair = factExecAndSet()
                resFactView.text = pair.first
                popup(v, "Время выполнения: ${pair.second} ms")
            } catch (e: TimeoutCancellationException) {
                resFactView.text = "Выполнение прервано: длилось дольше 1 сек"
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun factExecAndSet(): Pair<String, Long> {
        val startTime = System.currentTimeMillis()
        return withContext(IO) {
//           Корутина с таймером
            withTimeout(JOB_TIMEOUT) {
                fact(inputNum)
            }
        }

    }

    suspend fun fact(n: Int): Pair<String, Long> {
        val startTime = System.currentTimeMillis()
        var s: Int
        var y = 0.0
        val result: Pair<String, Long>
        s = Math.ceil(Math.sqrt(n.toDouble())).toInt()
        while (true) {
            y = Math.sqrt(Math.pow(s.toDouble(), 2.0) - n)
            if (y % 1 == 0.0) {
                result = Pair("""
                    x=${(s - y).toInt()}
                    y=${(s + y).toInt()}
                    """.trimIndent(),
                        System.currentTimeMillis() - startTime)
                return result
            }
            s++
            yield() //Приостановка ради возможности выбросить исключение
        }
    }

    fun popup(view: View?, message: String?) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window, null)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        (popupWindow.contentView.findViewById<View>(R.id.popupMessage) as TextView).text = message
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }


    override fun onBackPressed() {
        val intent = Intent(this@Fact, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
