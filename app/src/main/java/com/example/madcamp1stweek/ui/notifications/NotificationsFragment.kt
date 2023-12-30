package com.example.madcamp1stweek.ui.notifications

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp1stweek.R
import com.example.madcamp1stweek.databinding.FragmentNotificationsBinding
import com.example.madcamp1stweek.ui.home.HomeFragment

class RestaurantViewModel:ViewModel() {
    val loadedRestaurants = mutableListOf<HomeFragment.Restaurant>()
    init {
        // 초기 식당 데이터 로드
        loadedRestaurants.add(HomeFragment.Restaurant("식당1", "주소1", "010-1234-5678", ""))
        // 여기에 더 많은 식당 추가 가능
    }
    }
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var restaurantViewModel: RestaurantViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        restaurantViewModel =
            ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        val randomButton: Button = binding.root.findViewById(R.id.button)
        randomButton.setOnClickListener {
            showRandomRestaurantPopup()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRandomRestaurantPopup() {
        // restaurantViewModel 또는 loadedRestaurants가 null이면 처리하지 않음
        if (restaurantViewModel == null || restaurantViewModel.loadedRestaurants.isNullOrEmpty()) {
            // 예외 처리 또는 메시지 표시
            return
        }

        // 식당 목록(loadedRestaurants)에서 랜덤하게 하나를 선택
        val randomRestaurant = restaurantViewModel.loadedRestaurants.random()

        // 선택한 식당 정보를 다이얼로그(Dialog)로 표시
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Random Restaurant")
            .setMessage("Name: ${randomRestaurant.name}\nAddress: ${randomRestaurant.address}\nPhone: ${randomRestaurant.phoneNumber}")
            .setPositiveButton("OK", null)
            .create()

        dialog.show()
    }
}