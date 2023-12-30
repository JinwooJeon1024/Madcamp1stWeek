package com.example.madcamp1stweek.ui.notifications

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.madcamp1stweek.R
import com.example.madcamp1stweek.databinding.FragmentNotificationsBinding
import com.example.madcamp1stweek.ui.home.HomeFragment



class RestaurantViewModel:ViewModel() {
    val loadedRestaurants = mutableListOf<HomeFragment.Restaurant>()
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
        val randomRestaurant = restaurantViewModel.loadedRestaurants.random()
        // 선택한 식당 정보를 다이얼로그로 표시
        val layoutInflater = LayoutInflater.from(context)
        val popupView = layoutInflater.inflate(R.layout.restaurant_popup, null)  // 팝업용 레이아웃

        val nameTextView: TextView = popupView.findViewById(R.id.popupName)
        val addressTextView: TextView = popupView.findViewById(R.id.popupAddress)
        val phoneNumberTextView: TextView = popupView.findViewById(R.id.popupPhone)
        val imageView: ImageView = popupView.findViewById(R.id.popupImage)

        nameTextView.text = randomRestaurant.name
        addressTextView.text = randomRestaurant.address
        phoneNumberTextView.text = randomRestaurant.phoneNumber

        Glide.with(this).load(randomRestaurant.imageUrl).into(imageView)

        AlertDialog.Builder(requireContext()).apply {
            setView(popupView)
            setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
        }.create().show()
    }
}