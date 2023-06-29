package com.kosa.gather_e.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import com.kosa.gather_e.LoginActivity
import com.kosa.gather_e.databinding.FragmentHomeBinding
import com.kosa.gather_e.databinding.FragmentMypageBinding


class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myPageViewModel =
            ViewModelProvider(this)[MypageViewModel::class.java]

        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        myPageViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        myPageViewModel.profileImg.observe(viewLifecycleOwner) {
            Glide.with(this).load(it).into(binding.profileImg)
        }
        myPageViewModel.name.observe(viewLifecycleOwner) {
            binding.profileName.text = it
        }
        binding.logoutBtn.setOnClickListener {
            UserApiClient.instance.logout {
                startActivity(Intent(activity,LoginActivity::class.java))
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}