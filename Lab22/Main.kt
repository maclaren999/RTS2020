package Lab22

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
        val x = NumberAxis()
        val y = NumberAxis()
        val d = NumberAxis()
        val j = NumberAxis()
        val numberLineChart = LineChart(x, y)
        val numberLineChart1 = LineChart(d, j)
        val numberLineChart2 = LineChart(d, j)
        val signal = Series<Number, Number>()
        val fourier1 = Series<Number, Number>()
        val fourier2 = Series<Number, Number>()
        val data_x = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
        val data_dft = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
        val data_fft = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
        val X = graph()
        val dft = DFT(X)
        val fft = FFT(X)
        var a = 0.0
        var b = 0.0
        for (i in X.indices) {
            data_x.add(XYChart.Data<Number, Number>(i, X[i]))
            a = Math.sqrt(Math.pow(dft[i][0], 2.0) + Math.pow(dft[i][1], 2.0))
            b = Math.sqrt(Math.pow(fft[i][0], 2.0) + Math.pow(fft[i][1], 2.0))
            data_dft.add(XYChart.Data<Number, Number>(i, a))
            data_fft.add(XYChart.Data<Number, Number>(i, b))
        }
        // }
        signal.setData(data_x)
        signal.name = "Сигнал"
        fourier1.setData(data_dft)
        fourier1.name = "Дискрктне перетворення фур'є"
        fourier2.setData(data_fft)
        fourier2.name = "Швидке перетворення фур'є"
        numberLineChart.data.add(signal)

        numberLineChart1.data.add(fourier1)
        numberLineChart2.data.add(fourier2)
        val scene = Scene(numberLineChart)
        numberLineChart.createSymbols = false
        primaryStage.scene = scene
        primaryStage.show()
        val Scene1 = Scene(numberLineChart1, 0.0, 0.0)
        val Scene2 = Scene(numberLineChart2, 0.0, 0.0)
        numberLineChart1.createSymbols = false
        numberLineChart2.createSymbols = false
        val Window = Stage()
        val Window2 = Stage()
        Window.scene = Scene1
        Window.show()
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

        fun DFT(x: FloatArray): Array<DoubleArray> {
            val N = x.size
            val dft = Array(N) { DoubleArray(2) } //перший стовпчик - дійсна частина, другий - уявна
            for (p in 0 until N) {
                for (k in 0 until N) {
                    dft[p][0] += Math.cos(2 * Math.PI * p * k / N) * x[k]
                    dft[p][1] += Math.sin(2 * Math.PI * p * k / N) * x[k]
                }
            }
            return dft
        }

        fun FFT(x: FloatArray): Array<DoubleArray> {
            val N = x.size
            val fft = Array(N) { DoubleArray(2) } //перший стовпчик - дійсна частина, другий - уявна
            val array1 = DoubleArray(2)
            val array2 = DoubleArray(2)
            for (i in 0 until N / 2) {
                var cos: Double
                var sin: Double
                array2[1] = 0.0
                array2[0] = array2[1]
                array1[1] = array2[0]
                array1[0] = array1[1]
                for (j in 0 until N / 2) {
                    cos = Math.cos(4 * Math.PI * i * j / N)
                    sin = Math.sin(4 * Math.PI * i * j / N)
                    array1[0] += x[2 * j + 1] * cos //real
                    array1[1] += x[2 * j + 1] * sin //image
                    array2[0] += x[2 * j] * cos //real
                    array2[1] += x[2 * j] * sin //image
                }
                cos = Math.cos(2 * Math.PI * i / N)
                sin = Math.sin(2 * Math.PI * i / N)
                fft[i][0] = array2[0] + array1[0] * cos - array1[1] * sin //real
                fft[i][1] = array2[1] + array1[0] * sin + array1[1] * cos //image
                fft[i + N / 2][0] = array2[0] - (array1[0] * cos - array1[1] * sin) //real
                fft[i + N / 2][1] = array2[1] - (array1[0] * sin + array1[1] * cos) //image
            }
            return fft
        }

        @JvmStatic
        fun main(args: Array<String>) {
            launch(MyApplication::class.java, *args)        }
    }
}