package com.example.madcamp1stweek.ui.home
import android.app.Activity
import com.example.madcamp1stweek.R
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1stweek.databinding.FragmentHomeBinding
import com.example.madcamp1stweek.AddRestaurantActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val loadedRestaurants = mutableListOf<Restaurant>()

    private val ADD_RESTAURANT_REQUEST = 1  // 요청 코드 정의

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // JSON에서 식당 데이터 로드
        if (loadedRestaurants.isEmpty()) {
            loadedRestaurants.addAll(loadRestaurantsFromAssets(requireContext()))
        }

        // 리사이클러뷰 설정
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = RestaurantAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpaceItemDecoration(16))

        // 식당 등록하기 버튼 클릭 이벤트 처리
        binding.addRestaurantButton.setOnClickListener {
            val intent = Intent(context, AddRestaurantActivity::class.java)
            startActivityForResult(intent, ADD_RESTAURANT_REQUEST)
        }

        adapter.submitList(loadedRestaurants.sortedBy { it.name })

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_RESTAURANT_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let {
                val name = it.getStringExtra("name") ?: ""
                val phoneNumber = it.getStringExtra("phoneNumber") ?: ""
                val description = it.getStringExtra("description") ?: ""

                val newRestaurant = Restaurant(name, phoneNumber, description)  // 기본 설명 추가

                // 확장 함수를 사용하여 식당 추가 및 정렬
                loadedRestaurants.addAndSort(newRestaurant)

                // 어댑터에 알림을 보내 리스트를 갱신
                val adapter = binding.recyclerView.adapter as? RestaurantAdapter
                adapter?.submitList(loadedRestaurants.sortedBy { it.name })

            }
        }
    }

    fun MutableList<Restaurant>.addAndSort(newRestaurant: Restaurant) {
        add(newRestaurant)
        sortBy { it.name }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadRestaurantsFromAssets(context: Context): List<Restaurant> {
        val jsonString = context.assets.open("restaurants.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, object : TypeToken<List<Restaurant>>() {}.type)
    }

    data class Restaurant(val name: String, val phoneNumber: String, val description: String)

    class RestaurantAdapter : ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(RestaurantDiffCallback()) {
        // ViewHolder 및 기타 필요한 메서드 구현
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
            val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberTextView)
            val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            holder.nameTextView.text = item.name
            holder.phoneNumberTextView.text = item.phoneNumber
            holder.descriptionTextView.text = item.description
        }

        class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
            override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
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