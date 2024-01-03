package com.example.madcamp1stweek.ui.dashboard
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.madcamp1stweek.R
import com.example.madcamp1stweek.databinding.FragmentDashboardBinding
import androidx.fragment.app.viewModels
import android.app.AlertDialog
import android.graphics.PorterDuff
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Spinner
import androidx.core.content.ContextCompat

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val ADD_REVIEW_REQUEST = 1  // 요청 코드 정의
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val adapter = ReviewAdapter { itemData ->
            showItemDialog(itemData)
        }
        val numOfColumns = 2
        binding.galleryRecyclerView.apply {
            layoutManager = GridLayoutManager(context, numOfColumns)
            this.adapter = adapter
        }

        // Observe the viewModel's loadedReviews and update the adapter accordingly
        viewModel.loadedReviews.observe(viewLifecycleOwner) { reviews ->
            adapter.setReviews(reviews)
        }

        if (viewModel.loadedReviews.value.isNullOrEmpty()) {
            viewModel.loadReviews(requireContext())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageButton = view.findViewById<ImageButton>(R.id.addReviewButton)
        val color = context?.let { ContextCompat.getColor(it, R.color.white) } // 원하는 색상 리소스
        if (color != null) {
            imageButton.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        // 식당 등록하기 버튼 클릭 이벤트 처리
        binding.addReviewButton.setOnClickListener {
            val intent = Intent(context, AddReviewActivity::class.java)
            startActivityForResult(intent, ADD_REVIEW_REQUEST)
        }
        val spinner: Spinner = view.findViewById(R.id.spinner) // Assuming you have a spinner with this ID in your layout
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedOption = parent.getItemAtPosition(position) as String
                // Update the data based on selected option and refresh the RecyclerView
                sortData(selectedOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Do something when nothing is selected
            }
        }
    }
    private fun sortData(sortOption: String) {
        viewModel.loadedReviews.value?.let { reviews ->
            val sortedList = when (sortOption) {
                "이름순" -> reviews.sortedBy { it.name }
                "별점 높은 순" -> reviews.sortedByDescending { it.rating } // Assume you have a rating attribute or implement your logic
                "별점 낮은 순" -> reviews.sortedBy{ it.rating } // Assume you have a rating attribute or implement your logic
                else -> reviews
            }
            viewModel.loadedReviews.value = sortedList.toMutableList()
        }
    }
    private fun showItemDialog(itemData: ItemData) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_item, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        // dialog_item.xml의 각 뷰에 데이터 설정
        val imageView = dialogView.findViewById<ImageView>(R.id.dialogItemImage)
        val nameView = dialogView.findViewById<TextView>(R.id.dialogItemName)
        val ratingView = dialogView.findViewById<TextView>(R.id.dialogItemRating)
        val contentView = dialogView.findViewById<TextView>(R.id.dialogItemContent)

        // 여기서 이미지 로딩 라이브러리 사용 (예: Glide)
        Glide.with(this).load(itemData.imageUrl).into(imageView)
        nameView.text = itemData.name
        ratingView.text = itemData.rating
        contentView.text = itemData.content

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialoglist)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_REVIEW_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let {
                val imageUrl = it.getStringExtra("imageUrl") ?: ""
                val name = it.getStringExtra("name") ?: ""
                val rating = "⭐" + (it.getStringExtra("rating") ?: "")
                val content = it.getStringExtra("content") ?: ""
                val newItemData = ItemData(imageUrl, name, rating, content)

                viewModel.addReview(newItemData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}