package com.fahruaz.farmernusantara.ui.fragment.farmland

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.fahruaz.farmernusantara.R
import com.fahruaz.farmernusantara.databinding.FragmentFarmlandBinding
import com.fahruaz.farmernusantara.ui.DetailFarmlandActivity
import com.fahruaz.farmernusantara.ui.ImageConfirmationActivity

class FarmlandFragment : Fragment() {

    private var binding: FragmentFarmlandBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFarmlandBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tbFarmland?.findViewById<ImageView>(R.id.addBtn)?.setOnClickListener {
            val intent = Intent(requireContext(), DetailFarmlandActivity::class.java)
            startActivity(intent)
        }
    }

}