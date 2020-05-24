package Lab1

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.fxml.FXMLLoader
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
        primaryStage.title = "Окно"
        val x = NumberAxis()
        val y = NumberAxis()
        val `val` = graph()
        var timeBefore = System.nanoTime()
        val m = expectation(`val`)
        var timeAfter = System.nanoTime()
        val TM = timeAfter - timeBefore
        timeBefore = System.nanoTime()
        val d = disp(`val`, m)
        timeAfter = System.nanoTime()
        val TD = timeAfter - timeBefore
        println("""Матожидаение:
             |$m     ${TM}с
             |Дисперсия:
             |$d     ${TD}с""".trimMargin())
        val numberLineChart = LineChart(x, y)
        numberLineChart.title = "График"
        val series1: Series<Number, Number> = Series<Number, Number>()
        val datas = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
        for (i in `val`.indices) {
            datas.add(XYChart.Data<Number, Number>(i, `val`[i]))
        }
        series1.setData(datas)
        val scene = Scene(numberLineChart, 1500.0, 600.0)
        numberLineChart.data.add(series1)
        primaryStage.scene = scene
        primaryStage.show()
    }

    companion object {
        fun graph(): FloatArray {

            /*Variant*/
            val n = 10
            val W_max = 900
            val N = 256
            val x = FloatArray(N)
            val ran = Random()
            var A: Int
            var W: Int
            var f: Int
            for (i in 0 until n) {
                A = ran.nextInt(10)
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

        fun disp(x: FloatArray, m: Float): Float {
            var d = 0f
            for (i in x.indices) {
                d += Math.pow(x[i] - m.toDouble(), 2.0).toFloat()
            }
            d /= x.size.toFloat()
            return d
        }

        @JvmStatic
        fun main(args: Array<String>) {
            launch(MyApplication::class.java, *args)
        }
    }
}