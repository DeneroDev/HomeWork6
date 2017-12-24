package com.example.denero.classwork6

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.widget.ImageView
import java.io.File

/**
 * Created by DENERO on 21.12.2017.
 */
public class ShowPhotoFull private constructor() {
    open lateinit var instace:ShowPhotoFull
    init {
        instace = this
    }
    private object Holder{val INSTANCE = ShowPhotoFull()}

    companion object {
        val instance: ShowPhotoFull by lazy { Holder.INSTANCE }
    }

    fun showDialogAdd(context: Context, bitmap: Bitmap,file:File){
       /* var builder = AlertDialog.Builder(context)
        var inflater = LayoutInflater.from(context)
        var linearLayout = inflater.inflate(R.layout.show_full_image,null)
        builder.setView(linearLayout)
        var imgV: ImageView = linearLayout.findViewById(R.id.show_img)
        imgV.setImageBitmap(bitmap)
        builder.create()
        builder.show()*/
        var mOutputFileURI = FileProvider.getUriForFile(context,
                context.getPackageName() + ".my.package.name.provider",
                file)
        var intent = Intent(context,PhotoFullActivity::class.java)
        intent.setType("image/jpeg")
        intent.putExtra("data",mOutputFileURI)
        context.startActivity(intent)
    }
}