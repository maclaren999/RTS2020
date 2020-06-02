package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_genetic_alg.*
import java.lang.Exception
import java.lang.NumberFormatException
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

import java.util.*

class GenAlg : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genetic_alg)


    }

    fun onGoBtnClick(v: View) {

        var mutKoef = 0.2 //editTextMutKoef.text.toString().toDouble()
        var str = "1, 2, 3, 5" //coef.text.toString()
        var y = 1 //y.text.toString().toInt()
        var n = 4 //n.text.toString().toInt()
        try {
            mutKoef = editTextMutKoef.text.toString().toDouble()
            str = coef.text.toString()
            y = editTextY.text.toString().toInt()
            n = editTextN.text.toString().toInt()
        } catch (nfe: NumberFormatException) {
            Toast.makeText(applicationContext, nfe.message + "NumFormExep", Toast.LENGTH_LONG)
            Log.d("TAG1", nfe.message)
            return
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG)
            Log.d("TAG1", e.message)
            return
        }

        val JOB_TIMEOUT = 2000L
        try {
            val job = CoroutineScope(Dispatchers.Main).launch { withTimeout(JOB_TIMEOUT){exec(mutKoef, str, y, n)} }
        } catch (e: TimeoutCancellationException) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG)
            resGenView.text = "Timeout ${JOB_TIMEOUT} ms was reached!"
        }

    }

    private suspend fun exec(mutKoef: Double, str: String, y: Int, n: Int) {
        var str = str
        var y = y

        str = str.replace(" ", "")
        val strCoef = str.split(",").toTypedArray()
        val x = IntArray(strCoef.size)
        for (i in strCoef.indices) {
            x[i] = strCoef[i].toInt()
        }
        val roots = withContext(IO){ withTimeout(1000L){genetic(x, y, n, mutKoef)} }
        var res = "Цілі корені:"
        for (i in roots!!.indices) {
            res += roots[i].toString() + ", "
        }
        res += "\nПеревірка:\n"
        y = roots[0] * x[0]
        res += x[0].toString() + "*" + roots[0]
        for (i in 1 until roots.size - 1) {
            y += roots[i] * x[i]
            res += "+" + x[i] + "*" + roots[i]
        }
        res += "=$y"
        resGenView.text = res
    }

    companion object {
        suspend fun genetic(x: IntArray, y: Int, n: Int, mutKoef: Double): IntArray? {
            val r = Random()
            val population = Array(n) { IntArray(x.size) }
            var roots = IntArray(x.size)
            val res = IntArray(x.size + 1)
            var itter: Int
            var minItter = Int.MAX_VALUE
            var procent = 0.0
            var f: Boolean
            
            itter = 0
            /* Generate start population*/
            for (i in 0 until n) {
                for (j in 0 until population[i].size) {
                    population[i][j] = r.nextInt(y / 2)
                    yield()
                }
                //System.out.println();
            }
            f = true
            //System.out.println();
            while (f) {
                yield()
                itter++
                /*Calculation delta*/
                val deltas = IntArray(population.size)
                for (i in deltas.indices) {
                    deltas[i] = delta(x, y, population[i])
                    yield()
                    if (deltas[i] == 0) {
                        roots = population[i]
                        f = false
                    }
                }
                if (f) {
                    /*Search parents*/
                    val min = intArrayOf(deltas[0], deltas[1])
                    var par1 = population[0]
                    var par2 = population[1]
                    for (i in 2 until deltas.size) {
                        if (deltas[i] < min[0]) {
                            min[0] = deltas[i]
                            par1 = population[i]
                            yield()
                        }
                        if (deltas[i] != min[0] && deltas[i] < min[1]) {
                            min[1] = deltas[i]
                            par2 = population[i]
                        }
                    }

                    /*Сrossover */
                    val child = Array(2) { IntArray(x.size) }
                    for (j in x.indices) {
                        child[0] = par1
                        child[1] = par2
                        for (i in j until x.size - 1) {
                            yield()
                            val a = child[0][i]
                            child[0][i] = child[1][i]
                            child[1][i] = a
                            if (delta(x, y, child[0]) === 0) {
                                roots = child[0]
                                f = false
                            }
                            if (delta(x, y, child[1]) === 0) {
                                roots = child[1]
                                f = false
                            }
                        }
                    }

                    /*Mutation*/
                    for (j in 0 until (mutKoef * 100).toInt()) {
                        yield()
                        population[r.nextInt(population.size - 1)][x.size - 1] = r.nextInt(y / 2) //random change mutKoef individual of population
                    }
                }
            }
            if (itter < minItter) {
                minItter = itter
                procent = 100 * mutKoef / (n * x.size)
            }
            //}
            for (i in roots.indices) {
                yield()
                res[i] = roots[i]
            }
            res[res.size - 1] = procent.toInt()
            return res
        }

        suspend fun delta(x: IntArray, y: Int, coef: IntArray): Int {
            var delta = 0
            for (i in x.indices) {
                yield()
                delta += x[i] * coef[i]
            }
            delta = Math.abs(y - delta)
            return delta
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@GenAlg, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
