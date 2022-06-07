package com.fahruaz.farmernusantara.ui.fragment.farmland

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.ViewModelFactory
import com.fahruaz.farmernusantara.adapters.FarmlandsAdapter
import com.fahruaz.farmernusantara.databinding.FragmentFarmlandBinding
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.ui.CreateFarmlandActivity
import com.fahruaz.farmernusantara.ui.DetailFarmlandActivity
import com.fahruaz.farmernusantara.viewmodels.FarmlandViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FarmlandFragment : Fragment() {

    private var binding: FragmentFarmlandBinding? = null
    private val farmlandsAdapter by lazy {
        FarmlandsAdapter {
            detailIntent(it)
        }
    }
    private var userModel: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        farmlandViewModel = obtainViewModel(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFarmlandBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = requireActivity()
        binding?.farmlandViewModel = farmlandViewModel

        setUpRecyclerView()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        farmlandViewModel?.toastFarmland?.observe(requireActivity()) {
            try {
                if(it.isNotEmpty()) {
                    if(it == "Berhasil mengambil data farmland")
                        showToast(it)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        farmlandViewModel?.isLoading?.observe(requireActivity()) {
            showLoading(it)
        }

        binding?.tbFarmland?.findViewById<ImageView>(R.id.addBtn)?.setOnClickListener {
            val intent = Intent(requireActivity(), CreateFarmlandActivity::class.java)
            startActivity(intent)
        }
        activity?.findViewById<FloatingActionButton>(R.id.fabFarmland)?.setOnClickListener { }

        farmlandViewModel?.getUser()?.observe(requireActivity()) {
            userModel = it

            if(isAdded) {
                requestApiData(userModel?.id!!, userModel?.token!!)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if(requestApi && userModel != null) {
            requestApiData(userModel?.id!!, userModel?.token!!)
        }

        requestApi = false
    }

    private fun requestApiData(id: String, token: String) {
        farmlandViewModel?.getAllFarmlandByOwner(id, token)
        farmlandViewModel?.listFarmland?.observe(requireActivity()) {
            if(it.isNotEmpty()) {
                showFarmland()
                farmlandsAdapter.setData(it)
            }
            else {
                showNoFarmland()
            }
        }
    }

    private fun detailIntent(id: String) {
        val intent = Intent(requireContext(), DetailFarmlandActivity::class.java)
        intent.putExtra(EXTRA_FARMLAND_ID, id)
        startActivity(intent)
    }

    private fun setUpRecyclerView() {
        binding?.rvFarmland?.adapter = farmlandsAdapter
        binding?.rvFarmland?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun obtainViewModel(activity: FragmentActivity): FarmlandViewModel {
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        return ViewModelProvider(activity, ViewModelFactory(pref, requireContext()))[FarmlandViewModel::class.java]
    }

    private fun showNoFarmland() {
        binding?.rvFarmland?.visibility = View.GONE
        binding?.tvNoFarmland?.visibility = View.VISIBLE
    }

    private fun showFarmland() {
        binding?.rvFarmland?.visibility = View.VISIBLE
        binding?.tvNoFarmland?.visibility = View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading)
            binding?.pbFarmland?.visibility = View.VISIBLE
        else
            binding?.pbFarmland?.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        farmlandViewModel = null
    }

    companion object {
        var farmlandViewModel: FarmlandViewModel? = null
        var requestApi = true
        const val EXTRA_FARMLAND_ID = "EXTRA_FARMLAND_ID"
        const val EXTRA_FARMLAND = "EXTRA_FARMLAND"
    }

}