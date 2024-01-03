package com.example.madcamp1stweek.ui.home

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp1stweek.R

class RestaurantAdapter(private var loadedRestaurants: MutableList<Restaurant>, private val onRestaurantEditedListener: OnRestaurantEditedListener) : ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(RestaurantDiffCallback()) {
    interface OnRestaurantEditedListener {
        fun onRestaurantEdited(restaurant: Restaurant)
        fun onRestaurantDeleted(restaurantId: String)  // Add this line
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val addressTextView:TextView = itemView.findViewById(R.id.addressTextView)
        val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) // 현재 아이템을 가져옵니다.

        holder.nameTextView.text = item.name
        holder.addressTextView.text = item.address
        holder.phoneNumberTextView.text = item.phoneNumber

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            // 여기에서는 현재 아이템 위치의 데이터를 사용합니다.
            showEditDialog(holder.itemView.context, item)
        }
    }

    private fun showEditDialog(context: Context, restaurant: Restaurant) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_edit_restaurant, null)

        val imageView = view.findViewById<ImageView>(R.id.editRestaurantImage)
        val nameEditText = view.findViewById<EditText>(R.id.editRestaurantName)
        val addressEditText = view.findViewById<EditText>(R.id.editRestaurantAddress)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.editRestaurantPhoneNumber)
        val imageUrlEditText = view.findViewById<EditText>(R.id.editRestaurantImageUrl)  // Add this line

        // Set the current restaurant information in the dialog
        nameEditText.setText(restaurant.name)
        addressEditText.setText(restaurant.address)
        phoneNumberEditText.setText(restaurant.phoneNumber)
        imageUrlEditText.setText(restaurant.imageUrl)  // Add this line
        Glide.with(context).load(restaurant.imageUrl).into(imageView)

        val editDialog = AlertDialog.Builder(context).apply {
            setTitle("식당 정보 수정하기")
            setView(view)
            setPositiveButton("수정하기") { _, _ ->
                val updatedName = nameEditText.text.toString()
                val updatedAddress = addressEditText.text.toString()
                val updatedPhoneNumber = phoneNumberEditText.text.toString()
                val updatedImageUrl = imageUrlEditText.text.toString()  // Add this line

                val updatedRestaurant = restaurant.copy(
                    name = updatedName,
                    address = updatedAddress,
                    phoneNumber = updatedPhoneNumber,
                    imageUrl = updatedImageUrl  // Update this line
                )
                onRestaurantEditedListener.onRestaurantEdited(updatedRestaurant)
                showCustomToast(context, "수정되었습니다!")
            }
            setNegativeButton("취소", null)
        }.create()

        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            // Show confirmation dialog for deletion
            AlertDialog.Builder(context).apply {
                setTitle("식당 삭제하기")
                setMessage("삭제하시겠습니까 ?")
                setPositiveButton("삭제하기") { _, _ ->
                    onRestaurantEditedListener.onRestaurantDeleted(restaurant.id)  // Call the delete method
                    showCustomToast(context, "삭제되었습니다!")
                    editDialog.dismiss()  // Dismiss the edit dialog
                }
                setNegativeButton("취소", null)
            }.create().show()
        }

        editDialog.show()
    }

    fun showCustomToast(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(
            R.layout.custom_toast,  // 사용자 정의 토스트 레이아웃 파일
            null
        )

        val text: TextView = layout.findViewById(R.id.custom_toast_message) // 사용자 정의 토스트의 TextView
        text.text = message

        with(Toast(context)) {
            setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 860)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
    fun setRestaurants(list: List<Restaurant>){
        submitList(list)
    }
    class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            // 아이템의 고유한 속성이나 ID를 비교하여 동일한 아이템인지 판단합니다.
            // 예시: return oldItem.id == newItem.id
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            // 아이템의 데이터 내용이 같은지 비교합니다.
            // 예시: return oldItem == newItem
            return oldItem == newItem
        }
    }
}
