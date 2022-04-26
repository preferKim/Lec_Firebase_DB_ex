package com.preferkim.diet_memo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val writeBtn = findViewById<ImageView>(R.id.writeBtn)
        writeBtn.setOnClickListener {
            // custom dialog 띄우기
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("운동 메모 다이얼로그")

            val mAlertDialog = mBuilder.show()

            val dateSelectBtn = mAlertDialog.findViewById<Button>(R.id.dateSelectBtn)

            var dateText: String = ""

            dateSelectBtn?.setOnClickListener {

                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)

                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        dateSelectBtn.setText("${year}, ${month}, ${dayOfMonth}")

                        dateText = "${year}, ${month}, ${dayOfMonth}"
                    }

                }, year, month, date)

                dlg.show()

            }

            val saveBtn = mAlertDialog.findViewById<Button>(R.id.saveBtn)
            saveBtn?.setOnClickListener {

                val healthMemo =
                    mAlertDialog.findViewById<EditText>(R.id.healthMemo)?.text.toString()

                val database = Firebase.database
                val myRef = database.getReference("myMemo")

                val model = DataModel(dateText, healthMemo)

                myRef
                    .push()
                    .setValue(model)

//                myRef.setValue("Hello, World!")
//                myRef.push().setValue("Hello, World!") // 중복 허용하면서 데이터 추가

            }

        }

    }

}