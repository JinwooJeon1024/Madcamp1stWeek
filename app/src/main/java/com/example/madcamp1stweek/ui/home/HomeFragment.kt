package com.example.madcamp1stweek.ui.home
import com.example.madcamp1stweek.R
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1stweek.databinding.FragmentHomeBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val loadedRestaurants = loadRestaurantsFromAssets(requireContext())
        val sortedRestaurants = loadedRestaurants.sortedBy { it.name }
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = RestaurantAdapter(sortedRestaurants)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner) {
        //    textView.text = it
        //}
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun loadRestaurantsFromAssets(context: Context): List<Restaurant> {
        val jsonString: String = context.assets.open("restaurants.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val restaurantType = object : TypeToken<List<Restaurant>>() {}.type
        val loadedRestaurants = gson.fromJson<List<Restaurant>>(jsonString, restaurantType)
        Log.d("HomeFragment", "Loaded restaurants: $loadedRestaurants")
        return loadedRestaurants
    }
    data class Restaurant(val name: String, val phoneNumber: String, val description: String)
}
data class Restaurant(val name:String, val phoneNumber: String, val description: String)
    class RestaurantAdapter(private val items: List<HomeFragment.Restaurant>) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>(){
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
            val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberTextView)
            val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)  // description을 위한 TextView
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
            return ViewHolder(view)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.nameTextView.text = item.name
            holder.phoneNumberTextView.text = item.phoneNumber
            holder.descriptionTextView.text = item.description
        }

        override fun getItemCount(): Int {
            return items.size
        }

    }
class SpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = verticalSpaceHeight
    }
}
