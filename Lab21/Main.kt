package Lab21

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
    lateinit var W_real: Array<DoubleArray>
    lateinit var W_image: Array<DoubleArray>
    fun DFT(x: FloatArray): Array<DoubleArray> {
        val N = x.size
        val dft = Array(x.size) { DoubleArray(2) } //перший стовпчик - дійсна частина, другий - уявна
        coef(N)
        for (p in 0 until N) {
            for (k in 0 until N) {
                dft[p][0] += W_real[p][k] * x[k]
                dft[p][1] += W_image[p][k] * x[k]
            }
        }
        return dft
    }

    fun coef(N: Int) {
        W_real = Array(N) { DoubleArray(N) }
        W_image = Array(N) { DoubleArray(N) }
        for (i in 0 until N) {
            for (j in 0 until N) {
                W_real[i][j] = Math.cos(2 * Math.PI * i * j / N)
                W_image[i][j] = Math.sin(2 * Math.PI * i * j / N)
            }
        }
    }

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("sample.fxml"))
        val x = NumberAxis()
        val y = NumberAxis()
        val d = NumberAxis()
        val j = NumberAxis()
        val numberLineChart = LineChart(x, y)
        val numberLineChart1 = LineChart(d, j)
        val signal: Series<Number, Number> = Series<Number, Number>()
        val fourier: Series<Number, Number> = Series<Number, Number>()
        val data_x = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
        val data_dft = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
        val X = graph()
        val dft = DFT(X)
        var a = 0.0
        for (i in X.indices) {
            data_x.add(XYChart.Data<Number, Number>(i, X[i]))
            a = Math.sqrt(Math.pow(dft[i][0], 2.0) + Math.pow(dft[i][1], 2.0))
            data_dft.add(XYChart.Data<Number, Number>(i, a))
        }
        signal.setData(data_x)
        fourier.setData(data_dft)
        numberLineChart.data.add(signal)

        numberLineChart1.data.add(fourier)
        val scene = Scene(numberLineChart)
        primaryStage.scene = scene
        primaryStage.show()

        val Scene2 = Scene(numberLineChart1, 0.0, 0.0)

        val Window2 = Stage()
        Window2.scene = Scene2
        Window2.show()
    }

    companion object {
        fun graph(): FloatArray {

            //Variant
            val n = 10
            val W_max = 900
            val N = 256
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

        @JvmStatic
        fun main(args: Array<String>) {
            launch(MyApplication::class.java, *args)
        }
    }
}