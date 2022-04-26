package com.preferkim.diet_memo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    val dataModelList = mutableListOf<DataModel>() // DataModel 리스트 만들기

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // DB를 불러오기 위한 Firebase DB 연결
        firebaseAuth = FirebaseAuth.getInstance()
        val database = Firebase.database
        val myRef = database.getReference("myMemo")

        // ListView
        val listView = findViewById<ListView>(R.id.mainLV)

        // Main -> Adapter연결
        val adapter_list = ListViewAdapter(dataModelList)
        listView.adapter = adapter_list

        Log.d("Data-----", dataModelList.toString())


        // DB 불러오기 (사용자별)
        myRef.child(firebaseAuth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) { // snapshot : 모든 데이터를 가져옴

                Log.d("point", dataModelList.toString()) // 로그 계속 찍어보면서 공부
                dataModelList.clear() // 리스트뷰 덧씌워짐 방지
                Log.d("point", dataModelList.toString())

                for(dataModel in snapshot.children) { // 반복문을 이용해 snapshot의 내용을 꺼냄

                    /* Log 결과
                    D/Data: DataSnapshot { key = -N0ZpFxUwo3lpcwibAi2, value = {date=2022, 3, 20, memo=my health} }
                    D/Data: DataSnapshot { key = -N0ZpFxUwo3lpcwibAi2, value = {date=2022, 3, 20, memo=my health} }
                    D/Data: DataSnapshot { key = -N0ZsSSVw4cEgj_Ma8i0, value = {date=, memo=} }
                     */
                    Log.d("Data", dataModel.toString())
                    dataModelList.add(dataModel.getValue(DataModel::class.java)!!) // 내가 작성한 DataModel의 형태로 잘 들어옴

                }

                // 데이터모델리스트가 다 만들어지면 어댑터를 새롭게 만들어라
                adapter_list.notifyDataSetChanged()

                Log.d("DataModel", dataModelList.toString())

            }

            override fun onCancelled(error: DatabaseError) {


            }

        })

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
//                val myRef = database.getReference("myMemo")
                val myRef = database.getReference("myMemo").child(firebaseAuth.currentUser!!.uid) // 사용자별 DB 넣기


                val model = DataModel(dateText, healthMemo)

                myRef
                    .push()
                    .setValue(model)

                mAlertDialog.dismiss() // 다이얼로그 닫기

//                myRef.setValue("Hello, World!")
//                myRef.push().setValue("Hello, World!") // 중복 허용하면서 데이터 추가

            }

        }

    }

}