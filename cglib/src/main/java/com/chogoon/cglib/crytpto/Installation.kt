package com.chogoon.cglib.crytpto

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*

open class Installation {

    companion object {
        private var sID: String? = null

        @Synchronized
        fun id(context: Context): String? {
            if (sID == null) {
                val file = File(context.filesDir, "install")
                try {
                    if (!file.exists()) writeFile(file)
                    sID = readFile(file)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
            return sID
        }

        @Throws(IOException::class)
        private fun readFile(file: File): String? {
            val f = RandomAccessFile(file, "r")
            val bytes = ByteArray(f.length().toInt())
            f.readFully(bytes)
            f.close()
            return String(bytes)
        }

        @Throws(IOException::class)
        private fun writeFile(file: File) {
            val out = FileOutputStream(file)
            out.write(UUID.randomUUID().toString().toByteArray())
            out.close()
        }

        @JvmStatic
        fun getData(context: Context, isRead: Boolean, data: ByteArray): ByteArray? {
            var json: ByteArray? = null
            try {
                val file = File(context.filesDir, "_data")
                if (isRead) {
                    json = if (file.exists()) {
                        readFileData(file)
                    } else {
                        return null
                    }
                } else {
                    writeFileData(file, data)
                }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
            return json
        }

        @Throws(IOException::class)
        private fun readFileData(installation: File): ByteArray? {
            val f = RandomAccessFile(installation, "r")
            val data = ByteArray(f.length().toInt())
            f.readFully(data)
            f.close()
            return data
        }

        @Throws(IOException::class)
        private fun writeFileData(installation: File, data: ByteArray) {
            val out = FileOutputStream(installation)
            out.write(data)
            out.close()
        }
    }


}