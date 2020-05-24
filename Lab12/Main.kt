package Lab12

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.fxml.FXMLLoader
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.stage.Stage
import java.util.*

class MyApplication : Application() {
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("sample.fxml"))
        val x = NumberAxis()
        val y = NumberAxis()
        val t = NumberAxis()
        val cor = NumberAxis()
        val a = NumberAxis()
        val b = NumberAxis()
        var TM: Long = 0
        var TD: Long = 0
        val m = 0f
        val d = 0f


        var timeBefore = System.nanoTime()
        var timeAfter = System.nanoTime()
        TM = timeAfter - timeBefore
        timeBefore = System.nanoTime()
        timeAfter = System.nanoTime()
        TD = timeAfter - timeBefore
        var cor_xx: FloatArray
        var cor_xy: FloatArray

        val t_xy = LongArray(1000 - 256)
        val t_xx = LongArray(1000 - 256)
        for (i in 256..999) {
            val val_x = graph(i)
            val val_y = graph(i)
            timeBefore = System.nanoTime()
            cor_xy = cor(val_x, val_y)
            timeAfter = System.nanoTime()
            t_xy[i - 256] = timeAfter - timeBefore
            timeBefore = System.nanoTime()
            cor_xx = cor(val_x, val_x)
            timeAfter = System.nanoTime()
            t_xx[i - 256] = timeAfter - timeBefore
        }
        println("""Матожидаение:
             |$m     ${TM}с
             |Дисперсия:
             |$d     ${TD}с""".trimMargin())
        val numberLineChart = LineChart(x, y)
        val series1 = Series<Number, Number>()
        val series2= Series<Number, Number>()
        val series3 = Series<Number, Number>()
        val series4 = Series<Number, Number>()
        val data_x = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
        val data_y = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()

        for (i in 0 until 1000 - 256) {
            //if (time[i]<10000)
            if (t_xx[i] < 350000) data_x.add(XYChart.Data<Number, Number>(i + 256, t_xx[i]))
            if (t_xy[i] < 350000) data_y.add(XYChart.Data<Number, Number>(i + 256, t_xy[i]))
        }

series1.setData(data_x)
        series2.setData(data_y)

        series1.name = "xx"
        series2.name = "xy"
        series3.name = "cor xx"
        series4.name = "cor xy"
        val scene = Scene(numberLineChart, 1800.0, 600.0)
        numberLineChart.data.add(series1)
        numberLineChart.data.add(series2)

        primaryStage.scene = scene
        primaryStage.show()
        val root1 = Group()
        val root2 = Group()

    }

    companion object {
        fun graph(N: Int): FloatArray {

            /*Variant*/
            val n = 10
            val W_max = 900
            val x = FloatArray(N)
            val ran = Random()
            var A: Int
            var W: Int
            var f: Int
            for (i in 0 until n) {
                A = ran.nextInt(2)
                W = ran.nextInt(W_max)
                f = ran.nextInt()
                for (t in 0 until N) {
                    x[t] += (A * Math.sin((W * t + f).toDouble())).toFloat()
                }
            }
            return x
        }

        fun expectation(x: FloatArray): Float {
            var m = 0f
            for (i in x.indices) {
                m += x[i]
            }

            m /= x.size.toFloat()
            return m
        }

        fun expectation(x: FloatArray, t: Int): Float {
            var m = 0f
            for (i in t until x.size) {
                m += x[i]
            }

            m /= (x.size - t).toFloat()
            return m
        }

        fun disp(x: FloatArray, m: Float): Float {
            var d = 0f
            for (i in x.indices) {
                d += Math.pow(x[i] - m.toDouble(), 2.0).toFloat()
            }
            d /= x.size.toFloat()
            return d
        }

        fun cor(x: FloatArray, y: FloatArray): FloatArray {
            val R = FloatArray(x.size / 2)
            val Mx = expectation(x)
            val My = expectation(y)
            var t = 0
            while (t < x.size / 2 - 1) {
                R[t] = 0.0.toFloat()
                for (i in 0 until x.size / 2 - 1) {
                    R[t] += (x[i] - Mx) * (y[i + t] - My)
                }
                R[t]= R[t] / x.size - 1
                t++
                t++
            }
            return R
        }

        @JvmStatic
        fun main(args: Array<String>) {
            launch(MyApplication::class.java, *args)
        }
    }
}