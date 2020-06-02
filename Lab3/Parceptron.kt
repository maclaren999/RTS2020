package com.example.lab3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_parceptron.*

/*

Счетчик на строке 78, вывод на 47

*/

class Parceptron : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parceptron)
        val P = 4
        val pointsArr = arrayOf(intArrayOf(0, 6), intArrayOf(1, 5), intArrayOf(3, 3), intArrayOf(2, 4))
        val points = points
        var arr = "P=$P\n"
        for (i in pointsArr.indices) {
            arr += "("
            arr += pointsArr[i][0].toString() + "; "
            arr += pointsArr[i][1].toString() + "), "
        }

        points.text = arr
        val calc = button
        calc.setOnClickListener {
            val edTime = edTime
            val t = edTime.text.toString().toInt()
            val iter = edIter.text.toString().toInt()
            val sigma = edSigma.text.toString().toDouble()
            val result = parceptron(P, pointsArr, sigma, t.toLong(), iter)
            var res: String
            res = """
                w1=${result[0]}

                """.trimIndent()
            res += """
                w2=${result[1]}

                """.trimIndent()
            //вывод итераций
            res += """
                Количество итераций: ${result[2]}

                """.trimIndent()
            res += """
                Время: ${result[3]}

                """.trimIndent()
            resPercTextView.text = res
        }
    }

    override fun onBackPressed() {
            val intent = Intent(this@Parceptron, MainActivity::class.java)
            startActivity(intent)
            finish()
    }

    companion object {
        fun parceptron(P: Int, points: Array<IntArray>, sigma: Double, time: Long, iter: Int): DoubleArray {
            var delta: Double
            var y: Double
            var w1 = 0.0
            var w2 = 0.0
            //счетчик
            var j = 0
            val timeBefore = System.currentTimeMillis()
            var timeAfter: Long = 0
            for (k in 0 until iter) {
                run {
                    for (i in points.indices) {
                        //+итерация
                        j++
                        y = points[i][0] * w1 + points[i][1] * w2
                        if (y == Double.NEGATIVE_INFINITY || y == Double.POSITIVE_INFINITY) {
                            println()
                        }
                        delta = P - y
                        w1 = w1 + delta * sigma * points[i][0]
                        w2 = w2 + delta * sigma * points[i][1]
                        timeAfter = System.currentTimeMillis()
                        if (w1 == Double.NEGATIVE_INFINITY) {
                            println()
                        }
                        if (w1 != w1) {
                            val g = w1
                        }
                        if (w1 * points[0][0] + w2 * points[0][1] > P && w1 * points[1][0] + w2 * points[1][1] > P && w1 * points[2][0] + w2 * points[2][1] < P && w1 * points[3][0] + w2 * points[3][1] < P ||
                                timeAfter - timeBefore > time) {
                            return doubleArrayOf(w1, w2, j.toDouble(), timeAfter - timeBefore.toDouble())
                        }
                    }
                }
            }
            return doubleArrayOf(w1, w2, j.toDouble(), timeAfter - timeBefore.toDouble())
        }
    }
}