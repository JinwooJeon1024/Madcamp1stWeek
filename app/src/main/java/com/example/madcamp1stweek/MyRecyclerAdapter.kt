package com.example.madcamp1stweek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1stweek.R

class MyRecyclerAdapter(private val itemData: List<ItemData>) :
    RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {

    // ViewHolder 클래스 정의
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image)
        // 추가적으로 title, content 등을 여기에 포함시킬 수 있습니다.
    }

    // onCreateViewHolder() 메소드 구현
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    // onBindViewHolder() 메소드 구현
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemData[position]
        holder.imageView.setImageResource(item.image)
        // 추가적으로 title, content 등을 바인딩할 수 있습니다.
    }

    // getItemCount() 메소드 구현
    override fun getItemCount() = itemData.size
}
