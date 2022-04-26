package com.preferkim.diet_memo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.BaseAdapter
import android.widget.TextView

class ListViewAdapter(val List: MutableList<DataModel>) : BaseAdapter(){ // DataModel 타입의 리스트를 받겠다.

    override fun getCount(): Int {
        return List.size
    }

    override fun getItem(position: Int): Any {
        return List[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView
        if (convertView == null) {

            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.listview_item, parent, false)


        }

        // 레이아웃과 연결
        val date = convertView?.findViewById<TextView>(R.id.listViewDateArea)
        val memo = convertView?.findViewById<TextView>(R.id.listViewMemoArea)

        date!!.text = List[position].date // postion에 해당하는 데이터의 date
        memo!!.text = List[position].memo

        return convertView!!

    }

}