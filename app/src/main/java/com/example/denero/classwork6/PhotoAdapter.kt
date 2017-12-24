package com.example.denero.classwork6

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.view.*
import kotlinx.android.synthetic.main.photo_layout.view.*
import java.io.File
import android.widget.*
import android.content.Intent
import android.support.v4.content.FileProvider
import android.widget.EditText




/**
 * Created by DENERO on 18.12.2017.
 */
class PhotoAdapter(var data:ArrayList<File>):RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PhotoViewHolder =
            PhotoViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.photo_layout,parent,false))

    override fun onBindViewHolder(holder: PhotoViewHolder?, position: Int) {
        try {
            var bitmap = BitmapFactory.decodeFile(data.get(position).absolutePath)
            holder?.img?.setImageBitmap(bitmap)
            holder?.img?.setOnClickListener {
                if(bitmap!=null)
                {    ShowPhotoFull.instance.showDialogAdd(holder.v.context,bitmap,data.get(position))
                  }
                else
                    Toast.makeText(holder.v.context,"Поврежденные данные",Toast.LENGTH_LONG).show()
            }
                try {
                    holder?.img?.setOnLongClickListener {
                        val popup = PopupMenu(holder.v.getContext(), holder.v)
                        popup.inflate(R.menu.popup_menu)
                        popup.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener{
                            override fun onMenuItemClick(p0: MenuItem?): Boolean {

                                when(p0?.order) {
                                    100 ->{
                                       // Toast.makeText(holder.v.context,"Удалить",Toast.LENGTH_LONG).show()
                                        data.get(position).delete()
                                        data = DataNotify.instance.dataUpdate()
                                        notifyDataSetChanged()
                                    }
                                    99 ->{
                                       // Toast.makeText(holder.v.context,"Переименовать",Toast.LENGTH_LONG).show()
                                        var builder = AlertDialog.Builder(holder.v.context)
                                        var inflater = LayoutInflater.from(holder.v.context)
                                        var linearLayout = inflater.inflate(R.layout.create_dialog_rename,null)
                                        builder.setView(linearLayout)
                                                .setPositiveButton("OK",object: DialogInterface.OnClickListener{
                                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                                        var edtRename = linearLayout.findViewById<EditText>(R.id.edt_rename)
                                                        val str = edtRename.text.toString().replace(" ", "")
                                                        val file2 = File(data.get(position).toString().substring(0, data.get(position).toString().lastIndexOf("/")) + "/" + str + ".jpg")
                                                        data.get(position).renameTo(file2)
                                                        data = DataNotify.instance.dataUpdate()
                                                        notifyDataSetChanged()
                                                    }
                                                })
                                                .setNegativeButton("Cancel",object: DialogInterface.OnClickListener{
                                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                                        p0?.cancel()
                                                    }
                                                })

                                        builder.create()
                                        builder.show()
                                    }

                                    98 ->{
                                        var mOutputFileURI = FileProvider.getUriForFile(holder.v.context,
                                                holder.v.context.getPackageName() + ".my.package.name.provider",
                                                data.get(position))
                                        var intent = Intent(Intent.ACTION_SEND)
                                        intent.setType("image/jpeg")
                                        intent.putExtra(Intent.EXTRA_STREAM,mOutputFileURI)
                                        holder.v.context.startActivity(intent)
                                    }
                                    else->{
                                        Toast.makeText(holder.v.context, p0?.itemId.toString(),Toast.LENGTH_LONG).show()
                                    }
                                }
                                return true
                            }
                        })
                        popup.show()
                        true
                    }
                }catch (e:Exception){
                    Toast.makeText(holder?.v?.context,"Ошибка",Toast.LENGTH_LONG).show()
                }
                true
        }catch (e:OutOfMemoryError){
            Toast.makeText(holder?.v?.context,"Слишком много фотографий и файлов",Toast.LENGTH_LONG).show()
        }
    }



    override fun getItemCount(): Int {
       return data.size
    }

    class PhotoViewHolder(var v: View): RecyclerView.ViewHolder(v){
        var img: ImageView = v.photo




    }
}