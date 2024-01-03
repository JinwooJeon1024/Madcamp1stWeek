package com.example.madcamp1stweek.ui.home
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp1stweek.AddRestaurantActivity
import com.example.madcamp1stweek.R
import com.example.madcamp1stweek.RestaurantViewModel
import com.example.madcamp1stweek.databinding.FragmentHomeBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class HomeFragment : Fragment() {
    class RestaurantViewModel: ViewModel() {
        val loadedRestaurants = MutableLiveData<List<Restaurant>>()

        init {
            loadedRestaurants.value = listOf()
        }

        fun loadRestaurantsFromJSON(context: Context) {
            val jsonString = context.assets.open("restaurants.json").bufferedReader().use { it.readText() }
            loadedRestaurants.value = Gson().fromJson(jsonString, object : TypeToken<List<Restaurant>>() {}.type)
        }

        fun addRestaurant(newRestaurant: Restaurant) {
            val updatedList = ArrayList(loadedRestaurants.value ?: emptyList())
            updatedList.add(newRestaurant)
            updatedList.sortBy { it.name } // Or any other sorting if necessary
            loadedRestaurants.value = updatedList
        }
        fun updateRestaurant(updatedRestaurant: Restaurant) {
            val currentList = loadedRestaurants.value ?: return  // 현재 리스트를 가져옴
            val updatedList = currentList.toMutableList()
            // 해당 식당 찾기 및 업데이트
            val index = currentList.indexOfFirst { it.id == updatedRestaurant.id }
            if (index != -1) {
                updatedList[index] = updatedRestaurant
                loadedRestaurants.value = updatedList  // LiveData 업데이트
            }
        }
        fun deleteRestaurant(restaurantId: String) {
            val currentList = loadedRestaurants.value ?: return
            val updatedList = currentList.filterNot { it.id == restaurantId }
            loadedRestaurants.value = updatedList
        }
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()
    private val ADD_RESTAURANT_REQUEST = 1  // 요청 코드 정의

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러뷰 어댑터 설정
        val adapter = RestaurantAdapter(mutableListOf(), object :
            RestaurantAdapter.OnRestaurantEditedListener {
            override fun onRestaurantEdited(restaurant: Restaurant) {
                restaurantViewModel.updateRestaurant(restaurant)
            }
            override fun onRestaurantDeleted(restaurantId: String) {
                // Implement the deletion logic
                restaurantViewModel.deleteRestaurant(restaurantId)
            }
        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
            addItemDecoration(SpaceItemDecoration(16))
        }

        // ViewModel의 LiveData 관찰
        restaurantViewModel.loadedRestaurants.observe(viewLifecycleOwner) { updatedList ->
            adapter.setRestaurants(updatedList)
        }

        if (restaurantViewModel.loadedRestaurants.value.isNullOrEmpty()) {
            restaurantViewModel.loadRestaurantsFromJSON(requireContext())
        }
        binding.addRestaurantButton.setOnClickListener {
            val intent = Intent(context, AddRestaurantActivity::class.java)
            startActivityForResult(intent, ADD_RESTAURANT_REQUEST)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_RESTAURANT_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let {
                val id = UUID.randomUUID().toString() // UUID를 이용한 고유 ID 생성
                val name = it.getStringExtra("name") ?: ""
                val address = it.getStringExtra("address")?:""
                val phoneNumber = it.getStringExtra("phoneNumber") ?: ""
                val imageUrl = it.getStringExtra("imageUrl")?:""
                val newRestaurant = Restaurant(id, name, address, phoneNumber, imageUrl)  // 기본 설명 추가
                restaurantViewModel.addRestaurant(newRestaurant)
                // 어댑터에 알림을 보내 리스트를 갱신

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class Restaurant(val id: String, val name: String, val address: String, val phoneNumber: String, val imageUrl:String)

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
                        editDialog.dismiss()  // Dismiss the edit dialog
                    }
                    setNegativeButton("취소", null)
                }.create().show()
            }

            editDialog.show()
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
    class SpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
        // getItemOffsets 구현
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}