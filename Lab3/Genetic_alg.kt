package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab3.Genetic_alg
import java.util.*

class Genetic_alg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genetic_alg)
        val calc = findViewById<View>(R.id.calc) as Button
        calc.setOnClickListener {
            var str = (findViewById<View>(R.id.coef) as TextView).text.toString()
            var y = (findViewById<View>(R.id.y) as TextView).text.toString().toInt()
            val n = (findViewById<View>(R.id.n) as TextView).text.toString().toInt()
            str = str.replace(" ", "")
            val strCoef = str.split(",").toTypedArray()
            val x = IntArray(strCoef.size)
            for (i in strCoef.indices) {
                x[i] = strCoef[i].toInt()
            }
            val roots = genetic(x, y, n)
            var res = "Цілі корені:"
            for (i in roots.indices) {
                res += roots[i].toString() + ", "
            }
            res += "\nПеревірка:\n"
            y = roots[0] * x[0]
            res += x[0].toString() + "*" + roots[0]
            for (i in 1 until roots.size) {
                y += roots[i] * x[i]
                res += "+" + x[i] + "*" + roots[i]
            }
            res += "=$y"
            val resView = findViewById<View>(R.id.res) as TextView
            resView.text = res
        }
    }

    override fun onBackPressed() {
            val intent = Intent(this@Genetic_alg, MainActivity::class.java)
            startActivity(intent)
            finish()
    }

    companion object {
        fun genetic(x: IntArray, y: Int, n: Int): IntArray {
            val r = Random()
            val roots = Array(n) { IntArray(x.size) }

            /* Generate start population*/while (true) {
                for (i in 0 until n) {
                    for (j in 0 until roots[i].size) {
                        roots[i][j] = r.nextInt(y / 2)
                        print(roots[i][j].toString() + " ")
                    }
                    println()
                }
                println()

                /*Calculation delta*/
                val deltas = IntArray(roots.size)
                for (i in deltas.indices) {
                    deltas[i] = delta(x, y, roots[i])
                    if (deltas[i] == 0) {
                        return roots[i]
                    }
                }

                /*Search parents*/
                val min = intArrayOf(deltas[0], deltas[1])
                var par1 = roots[0]
                var par2 = roots[1]
                for (i in 2 until deltas.size) {
                    if (deltas[i] < min[0]) {
                        min[0] = deltas[i]
                        par1 = roots[i]
                    }
                    if (deltas[i] != min[0] && deltas[i] < min[1]) {
                        min[1] = deltas[i]
                        par2 = roots[i]
                    }
                }

                /*Сrossbreeding */
                val child = Array(2) { IntArray(x.size) }
                for (j in x.indices) {
                    child[0] = par1
                    child[1] = par2
                    for (i in j until x.size - 1) {
                        val a = child[0][i]
                        child[0][i] = child[1][i]
                        child[1][i] = a
                        if (delta(x, y, child[0]) == 0) {
                            return child[0]
                        }
                        if (delta(x, y, child[1]) == 0) {
                            return child[1]
                        }
                    }
                }
            }
        }

        fun delta(x: IntArray, y: Int, coef: IntArray): Int {
            var delta = 0
            for (i in x.indices) {
                delta += x[i] * coef[i]
            }
            delta = Math.abs(y - delta)
            return delta
        }
    }
}