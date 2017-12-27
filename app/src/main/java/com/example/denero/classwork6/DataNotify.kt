package com.example.denero.classwork6

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Debug
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.util.Log
import java.io.File
import java.io.FileOutputStream

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

    fun dataUpdate(list:RecyclerView){
        var data:ArrayList<File>
        data = listFilesWithSubFolders(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath))
        list.adapter = PhotoAdapter(data)
        list.adapter.notifyDataSetChanged()
    }

    fun listFilesWithSubFolders(dir: File): ArrayList<File> {
        val files = ArrayList<File>()
        for (file in dir.listFiles()) {
            if (file.isDirectory)
                files.addAll(listFilesWithSubFolders(file))
            else
            {
                if(file.absolutePath.endsWith(".jpg") || file.absolutePath.endsWith(".png") || file.absolutePath.endsWith(".jpeg"))
                    files.add(file)
                else
                    Log.d("FILE_END:",file.absolutePath.endsWith("jpg").toString())
            }
        }
        return files
    }
}
