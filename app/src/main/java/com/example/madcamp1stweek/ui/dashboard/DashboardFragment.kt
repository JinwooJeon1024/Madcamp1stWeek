package com.example.madcamp1stweek.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madcamp1stweek.GridSpacingItemDecoration
import com.example.madcamp1stweek.ItemData
import com.example.madcamp1stweek.MyRecyclerAdapter
import com.example.madcamp1stweek.R
import com.example.madcamp1stweek.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val recyclerView: RecyclerView = root.findViewById(R.id.galleryRecyclerView)

        val itemData = listOf(
            ItemData(R.drawable.image1, "Title 1", "Content 1"),
            ItemData(R.drawable.image2, "Title 2", "Content 2"),
            ItemData(R.drawable.image3, "Title 2", "Content 2"),
            ItemData(R.drawable.image4, "Title 2", "Content 2"),
            ItemData(R.drawable.image5, "Title 2", "Content 2"),
            ItemData(R.drawable.image6, "Title 1", "Content 1"),
            ItemData(R.drawable.image7, "Title 1", "Content 1"),
            ItemData(R.drawable.image8, "Title 2", "Content 2"),
            ItemData(R.drawable.image9, "Title 2", "Content 2"),
            ItemData(R.drawable.image10, "Title 2", "Content 2"),
            ItemData(R.drawable.image11, "Title 2", "Content 2"),
            ItemData(R.drawable.image12, "Title 2", "Content 2"),
            ItemData(R.drawable.image13, "Title 2", "Content 2"),
            ItemData(R.drawable.image14, "Title 2", "Content 2"),
            ItemData(R.drawable.image15, "Title 2", "Content 2"),
        )

        val numOfColumns = 3
        recyclerView.layoutManager = GridLayoutManager(context, numOfColumns)
        recyclerView.adapter = MyRecyclerAdapter(itemData)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, 50, true))
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}