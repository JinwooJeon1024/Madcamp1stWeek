package com.example.madcamp1stweek.ui.notifications

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.madcamp1stweek.R
import com.example.madcamp1stweek.RestaurantViewModel
import com.example.madcamp1stweek.databinding.FragmentNotificationsBinding
import com.example.madcamp1stweek.ui.home.HomeFragment

class NotificationsFragment : Fragment() {
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()
    private var _binding: FragmentNotificationsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val randomButton: Button = binding.root.findViewById(R.id.button)
        randomButton.setOnClickListener {
            showRandomRestaurantPopup()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // LiveData를 관찰하고 UI 업데이트
        restaurantViewModel.loadedRestaurants.observe(viewLifecycleOwner) { restaurants ->
            // 데이터가 변경될 때마다 할 작업 (예: UI 업데이트)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRandomRestaurantPopup() {
        val loadedRestaurants = restaurantViewModel.loadedRestaurants.value

        if (loadedRestaurants.isNullOrEmpty()) {
            Toast.makeText(context, "식당 목록이 비어 있습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val randomRestaurant = loadedRestaurants.random()
        // 선택한 식당 정보를 다이얼로그로 표시

        val popupView = layoutInflater.inflate(R.layout.restaurant_popup, null)  // 팝업용 레이아웃

        popupView.findViewById<TextView>(R.id.popupName).text = randomRestaurant.name
        popupView.findViewById<TextView>(R.id.popupAddress).text = randomRestaurant.address
        popupView.findViewById<TextView>(R.id.popupPhone).text = randomRestaurant.phoneNumber

        Glide.with(requireContext()).load(randomRestaurant.imageUrl).into(popupView.findViewById<ImageView>(R.id.popupImage))

        AlertDialog.Builder(requireContext()).apply {
            setView(popupView)
            setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
        }.create().show()
    }
}