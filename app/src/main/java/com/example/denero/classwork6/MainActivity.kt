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
import android.view.*
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
                    list.adapter = PhotoAdapter(data)
                    list.adapter.notifyDataSetChanged()
                    true
                }catch (e:OutOfMemoryError){
                    Toast.makeText(this,"Слишком много фотографий и файлов",Toast.LENGTH_LONG).show()
                    false
                }
            }
            R.id.download_img_url ->{
                var builder = AlertDialog.Builder(this)
                var inflater = this.layoutInflater
                var linearLayout = inflater.inflate(R.layout.create_dialog_uri,null)
                var edtUri = linearLayout.findViewById<EditText>(R.id.edt_rename)
                builder.setView(linearLayout)
                        .setPositiveButton("OK",object: DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                val url = edtUri.text.toString()
                                if (url == "" || ! url.endsWith(".jpg")) {
                                    Toast.makeText(applicationContext,
                                            "Please enter a valid .jpg link",
                                            Toast.LENGTH_SHORT).show()
                                } else {
                                    val task = GetImageByURLTask()
                                    task.execute(url)
                                }
                            }
                        })
                        .setNegativeButton("Cancel",object: DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0?.cancel()
                            }
                        })

                builder.create()
                builder.show()
                true
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

    inner class GetImageByURLTask : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg p0: String?): Bitmap {
            val file = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM),
                    System.currentTimeMillis().toString() + ".jpg"
            )
            file.createNewFile()

            val outStream = FileOutputStream(file)

            val url = URL(p0[0])
            outStream.write(url.readBytes())
            outStream.close()

            return BitmapFactory.decodeFile(file.absolutePath)
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            DataNotify.instance.dataUpdate(list)
        }
    }

}


