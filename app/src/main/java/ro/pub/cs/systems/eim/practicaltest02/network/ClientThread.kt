package ro.pub.cs.systems.eim.practicaltest02.network

import android.util.Log
import android.widget.TextView
import ro.pub.cs.systems.eim.practicaltest02.utils.Constants
import ro.pub.cs.systems.eim.practicaltest02.utils.Utils
import java.io.IOException
import java.net.Socket

class ClientThread(
    val address: String,
    val port: Int,
    val city: String,
    val information: String,
    val textView: TextView
) : Thread() {
    override fun run() {
        try {
            val socket = Socket(address, port)
            val writer = Utils.getWriter(socket)
            val reader = Utils.getReader(socket)
            writer.println(city)
            writer.flush()
            writer.println(information)
            writer.flush()
            val response = reader.readLine()
            textView.post {
                textView.text = response
            }
            socket.close()
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "An exception has occurred: ${ioException.message}")
        }
    }
}