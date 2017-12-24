package com.example.denero.classwork6

import android.os.Environment
import java.io.File

/**
 * Created by DENERO on 24.12.2017.
 */
public class DataNotify private constructor(){
    open lateinit var instance:DataNotify
    init {
        instance = this
    }
    private object Holder{val INSTANCE = DataNotify()}

    companion object {
        val instance: DataNotify by lazy { DataNotify.Holder.INSTANCE }
    }
    fun dataUpdate():ArrayList<File>{
        var data:ArrayList<File>
        data = listFilesWithSubFolders(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath))
        return data
    }

    fun dataNotify(dir: File):ArrayList<File>{
        var data:ArrayList<File>
        data = listFilesWithSubFolders(dir)
        return data
    }
    fun listFilesWithSubFolders(dir: File): ArrayList<File> {
        val files = ArrayList<File>()
        for (file in dir.listFiles()) {
            if (file.isDirectory)
                files.addAll(listFilesWithSubFolders(file))
            else
                files.add(file)
        }
        return files
    }
}