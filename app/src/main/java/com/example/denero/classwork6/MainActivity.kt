package com.example.denero.classwork6

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.Preference
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import java.net.URL
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import java.nio.file.Files.isDirectory
import java.nio.file.Files.isDirectory





class MainActivity : AppCompatActivity() {
    lateinit var btnTakePhoto:Button
    lateinit var list:RecyclerView
    lateinit var data:ArrayList<File>
    lateinit var mOutputFileURI:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTakePhoto = btn_take_photo
        data = ArrayList(300)
        var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath)
         data =DataNotify.instance.dataNotify(file)

        list = recView as RecyclerView
        list.layoutManager = GridLayoutManager(applicationContext,4)
        list.adapter = PhotoAdapter(data)
        list.adapter.notifyDataSetChanged()



        btnTakePhoto.setOnClickListener {
            saveFullImage()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        list.adapter = PhotoAdapter(DataNotify.instance.dataUpdate())
        list.adapter.notifyDataSetChanged()
    }

    fun saveFullImage(){
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        ,"hw_6"+getPhotoMass().toString()+".jpg")
        newPhotoMass()
        mOutputFileURI = FileProvider.getUriForFile(applicationContext,
                applicationContext.getApplicationContext().getPackageName() + ".my.package.name.provider",
                file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mOutputFileURI)
        startActivityForResult(intent,1)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.show_all_photo -> {
                try {
                    var file = File(Environment.getExternalStorageDirectory().absolutePath)
                    data = DataNotify.instance.dataNotify(file)
                    list.adapter.notifyDataSetChanged()
                    true
                }catch (e:OutOfMemoryError){
                    Toast.makeText(this,"Слишком много фотографий и файлов",Toast.LENGTH_LONG).show()
                    false
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getPhotoMass():Int{

        return getPreferences(Context.MODE_PRIVATE).getInt("MASS",10)
    }
    fun newPhotoMass(){
        getPreferences(Context.MODE_PRIVATE).edit()
                .putInt("MASS",getPhotoMass()+1)
                .apply()
    }

}


