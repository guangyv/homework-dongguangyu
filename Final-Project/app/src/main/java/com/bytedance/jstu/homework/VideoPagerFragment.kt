package com.bytedance.jstu.homework

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bytedance.jstu.homework.databinding.FragmentMainBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates

/**
 * A placeholder fragment containing a simple view.
 */
class VideoPagerFragment(private val position: Int) : Fragment() {

    private val TAG = "VideoPagerFragment"

    val retrofit = Retrofit.Builder()
        .baseUrl("https://bd-open-lesson.bytedance.com/api/invoke/")
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
//            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
//        }
        Log.d(TAG, "onCreate: ")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

//        val textView: TextView = binding.sectionLabel
//        textView.text = "VideoPagerFragment" + (arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)

        Log.d(TAG, "onCreateView: ")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        getVideo()
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(position: Int): VideoPagerFragment {
            Log.d("VideoPagerFragment", "newInstance: ")
            return VideoPagerFragment(position)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "onDestroyView: ")
    }

    private fun getVideo() {
        var studentID = ""
        if (position == 2) {
            studentID = "518051910008_post"
        }
        retrofit.create(VideoService::class.java)
            .getVideo(studentID)
            .enqueue(object : Callback<VideoBean> {
                override fun onResponse(call: Call<VideoBean>, response: Response<VideoBean>) {
                    val videoList = response.body()!!.feeds
                    val recyclerViewVideo = binding.recyclerView
                    recyclerViewVideo.layoutManager = LinearLayoutManager(binding.root.context)
                    recyclerViewVideo.adapter = VideoItemAdapter(videoList)
                }
                override fun onFailure(call: Call<VideoBean>, t: Throwable) {
                    Log.d(TAG, "onFailure: ")
                }
            })
    }

}