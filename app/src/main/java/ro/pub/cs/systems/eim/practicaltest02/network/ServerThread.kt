package ro.pub.cs.systems.eim.practicaltest02.network

import android.util.Log
import ro.pub.cs.systems.eim.practicaltest02.utils.Constants
import ro.pub.cs.systems.eim.practicaltest02.utils.WeatherInfo
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

class ServerThread(private val port: Int) : Thread() {

    var isRunning = false
    private var serverSocket: ServerSocket? = null
    private val data: HashMap<String, WeatherInfo> = HashMap()

    fun getData(): HashMap<String, WeatherInfo> {
        return data
    }

    fun startServer() {
        isRunning = true
        start()
        Log.v(Constants.TAG, "startServer() method was invoked")
    }

    fun stopServer() {
        isRunning = false
        try {
            serverSocket?.close()
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "An exception has occurred: ${ioException.message}")
            if (Constants.DEBUG) {
                ioException.printStackTrace()
            }
        }
        Log.v(Constants.TAG, "stopServer() method was invoked")
    }

    override fun run() {
        try {
            serverSocket = ServerSocket(this.port)
            while (isRunning) {
                val socket: Socket? = serverSocket?.accept()
                if (socket != null) {
                    val communicationThread = CommunicationThread(this, socket)
                    communicationThread.start()
                }
            }
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "An exception has occurred: ${ioException.message}")
            if (Constants.DEBUG) {
                ioException.printStackTrace()
            }
        }
    }
}

