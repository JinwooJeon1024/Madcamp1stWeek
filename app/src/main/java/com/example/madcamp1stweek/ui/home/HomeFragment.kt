package com.example.madcamp1stweek.ui.home
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1stweek.databinding.FragmentHomeBinding
import java.util.UUID

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val restaurantViewModel: RestaurantViewModel by viewModels()
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
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class SpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
        // getItemOffsets 구현
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}