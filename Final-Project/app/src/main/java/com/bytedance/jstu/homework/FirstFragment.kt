package com.bytedance.jstu.homework

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bytedance.jstu.homework.R
import com.bytedance.jstu.homework.databinding.FragmentFirstBinding
import com.google.android.material.tabs.TabLayout


class FirstFragment : Fragment() {

    private val TAG = "FirstFragment"

    private lateinit var viewPager: ViewPager
    private lateinit var videoPagerAdapter: VideoPagerAdapter

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val root: View = binding.root

        videoPagerAdapter = VideoPagerAdapter(binding.root.context, childFragmentManager)
        viewPager = binding.viewPager
        viewPager.adapter = videoPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}