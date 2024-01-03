package com.example.madcamp1stweek.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.madcamp1stweek.R
import com.example.madcamp1stweek.ui.home.RestaurantViewModel
import com.example.madcamp1stweek.databinding.FragmentNotificationsBinding

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

        navigateToGameScreen()
    }

    private fun navigateToGameScreen(){
        val intent = Intent(context, GameActivity::class.java)
        startActivity(intent)
    }
}