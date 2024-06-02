package com.santhi.collegenetwork.ui.postUpload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.adapter.CommunityAdapter
import com.santhi.collegenetwork.businessLogic.adapter.MyCommunityAdapter
import com.santhi.collegenetwork.businessLogic.adapter.SelectCommunityAdapter
import com.santhi.collegenetwork.businessLogic.model.CommunityModel
import com.santhi.collegenetwork.businessLogic.viewModel.CommunityViewModel
import com.santhi.collegenetwork.businessLogic.viewModel.MyCommunityViewModel
import com.santhi.collegenetwork.databinding.ActivitySelectClubBinding

class SelectClubActivity : AppCompatActivity() {
    private lateinit var viewModel: CommunityViewModel
    private lateinit var myCommunityViewModel: MyCommunityViewModel
    private lateinit var binding:ActivitySelectClubBinding
    private var filterList: MutableList<CommunityModel> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySelectClubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[CommunityViewModel::class.java]
        var postText:String? = null
        var postImg:String?= null
        val isAnonymously = intent.getBooleanExtra("isAnonymously",false)
        val intentType = intent.getStringExtra("type")
       // Toast.makeText(this, intentType, Toast.LENGTH_SHORT).show()

        if (intentType=="0"){
            postText = intent.getStringExtra("postText")
            postImg = intent.getStringExtra("url")
        }else if (intentType=="1"){
            postText = "noText"
            postImg = intent.getStringExtra("url")
        }else{
            postText = intent.getStringExtra("postText")
            postImg = "NoImg"
        }
       val adapter = SelectCommunityAdapter(this,postText.toString(),postImg.toString(),isAnonymously)
        myCommunityViewModel = ViewModelProvider(this)[MyCommunityViewModel::class.java]
//        val comAd = CommunityAdapter(requireContext())
         binding.selectRv.layoutManager = LinearLayoutManager(this)

//
//        val myComAd = MyCommunityAdapter(requireContext())
        viewModel.dataList.observe(this) { communities1 ->
            if (communities1.size>=1){
                binding.selectClub.visibility = View.GONE
            }
            filterList.clear()
            filterList.addAll(communities1)



            // Observe myCommunityViewModel.dataList after the initial data is loaded
            myCommunityViewModel.dataList.observe(this) { communities ->
                val necessaryItemIdsSet: Set<String> = communities
                    .mapNotNull { it.clubkey?.toIntOrNull()?.toString() }
                    .toSet()

                val filteredItems: List<CommunityModel> = filterList.filter { item ->
                    necessaryItemIdsSet.contains(item.id.toString())
                }


                adapter.updateList(filteredItems)
            }
        }
        binding.selectRv.adapter= adapter
    }
}