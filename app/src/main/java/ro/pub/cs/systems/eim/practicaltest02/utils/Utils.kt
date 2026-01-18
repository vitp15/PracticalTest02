package ro.pub.cs.systems.eim.practicaltest02.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

object Utils {
    @Throws(IOException::class)
    fun getReader(socket: Socket): BufferedReader {
        return BufferedReader(InputStreamReader(socket.getInputStream()))
    }

    @Throws(IOException::class)
    fun getWriter(socket: Socket): PrintWriter {
        return PrintWriter(socket.getOutputStream(), true)
    }
}

