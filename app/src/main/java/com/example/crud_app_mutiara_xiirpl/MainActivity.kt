package com.example.crud_app_mutiara_xiirpl

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.crud_app_mutiara_xiirpl.helper.DBHelper
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    //inisialisasi nilai (lateinit)
    lateinit var inputName: TextInputEditText
    lateinit var inputAge: TextInputEditText
    lateinit var btnAdd: Button
    lateinit var btnPrint: Button
    lateinit var textName: TextView
    lateinit var textAge: TextView
    lateinit var textId: TextView
    var progressDialog: ProgressDialog? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //inisialisasikan widget
        inputName = findViewById(R.id.inputName)
        inputAge = findViewById(R.id.inputAge)
        btnAdd = findViewById(R.id.btnAdd)
        btnPrint = findViewById(R.id.btnPrint)
        textId = findViewById(R.id.Id)
        textName = findViewById(R.id.name)
        textAge = findViewById(R.id.age)


        //loadHandler = ketika aplikasi di load tampilkan tabel
        loadHandler()

        //event ketika button add di click
        btnAdd.setOnClickListener {
            val db = DBHelper(this, null)
            val name = inputName.text.toString()
            val age = inputAge.text.toString()

            db.addProfile(name, age)

            inputName.text!!.clear()
            inputAge.text!!.clear()
        }

        btnPrint.setOnClickListener {
            loadHandler()
        }

    }

    @SuppressLint("Range")
    fun loadHandler() { //mengambil data di dalam tebel profile
        val db = DBHelper(this, null)
        val cursor = db.getProfile()

        //progres dialog
        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog!!.setTitle("Loading")
        progressDialog!!.setMessage("Tunggu Sebentar... Data Akan Muncul")
        progressDialog!!.max = 100
        progressDialog!!.progress = 1
        progressDialog!!.show()

        Thread(Runnable {
            try {
                Thread.sleep(1000)
            }catch (e:Exception){
                e.printStackTrace()
            }
            progressDialog!!.dismiss()
        }).start()

        if(cursor!!.moveToFirst()) { // mengambil data yang pertama kali muncul
            textId.text= "ID\n"
            textName.text= "Name\n"
            textAge.text= "Age\n"
            textId.append(
                cursor.getString(
                    cursor.getColumnIndex(DBHelper.ID_COL)) + "\n")
            textName.append(
                cursor.getString(
                    cursor.getColumnIndex(DBHelper.NAME_COL)) + "\n")
            textAge.append(
                cursor.getString(
                    cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
        }

        if(cursor!!.moveToNext()) {
            while (cursor.moveToNext()) {
                textId.append(
                    cursor.getString(
                        cursor.getColumnIndex(DBHelper.ID_COL)) + "\n")
                textName.append(
                    cursor.getString(
                        cursor.getColumnIndex(DBHelper.NAME_COL)) + "\n")
                textAge.append(
                    cursor.getString(
                        cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
            }
        }
        cursor.close()
    }
}