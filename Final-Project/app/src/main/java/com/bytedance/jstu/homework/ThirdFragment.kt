package com.bytedance.jstu.homework

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bytedance.jstu.homework.R
import com.bytedance.jstu.homework.databinding.FragmentThirdBinding
import kotlin.math.log

class ThirdFragment : Fragment() {

    private lateinit var account: EditText
    private lateinit var password: EditText
    private lateinit var remember: CheckBox
    private lateinit var login: Button

    private var _binding: FragmentThirdBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        val root: View = binding.root

        account = binding.account
        password = binding.password
        remember = binding.remember
        login = binding.login

        val pref = activity!!.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val isRemember = pref.getBoolean("remember_pw", false)
        if (isRemember) {
            account.setText(pref.getString("account", ""))
            password.setText(pref.getString("password", ""))
            remember.isChecked = true
            isLogined = true
        }

        login.setOnClickListener {
            if (account.text.isEmpty() || password.text.isEmpty()) {
                Toast.makeText(activity, "account or password can not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val editor = pref.edit()
            if ("admin" == account.text.toString() && "123456" == password.text.toString()) {
                if (remember.isChecked) {
                    editor.putBoolean("remember_pw", true)
                    editor.putString("account", account.text.toString())
                    editor.putString("password", password.text.toString())
                } else {
                    editor.putBoolean("remember_pw", false)
                }
                editor.apply()
                isLogined = true
                Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "account or password is not correct, please try again", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var isLogined = false
    }
}