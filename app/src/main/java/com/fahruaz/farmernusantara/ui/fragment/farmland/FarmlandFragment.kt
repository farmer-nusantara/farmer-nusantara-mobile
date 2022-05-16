package com.fahruaz.farmernusantara.ui.fragment.farmland

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.FragmentFarmlandBinding
import com.fahruaz.farmernusantara.ui.CreateFarmlandActivity
import com.fahruaz.farmernusantara.ui.DetailFarmlandActivity
import com.fahruaz.farmernusantara.ui.ImageConfirmationActivity
import com.fahruaz.farmernusantara.ui.VerificationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FarmlandFragment : Fragment() {

    private var binding: FragmentFarmlandBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFarmlandBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tbFarmland?.findViewById<ImageView>(R.id.addBtn)?.setOnClickListener {
            val intent = Intent(requireContext(), CreateFarmlandActivity::class.java)
            startActivity(intent)
        }

        Log.e("FarmlandFragment", "ada")

        activity?.findViewById<FloatingActionButton>(R.id.fabFarmland)?.setOnClickListener { }
    }

}