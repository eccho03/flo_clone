package org.techtown.flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import org.techtown.flo.databinding.FragmentLockerBinding
import kotlin.math.log

class LockerFragment : Fragment() {
       lateinit var binding : FragmentLockerBinding
       private val informaiton = arrayListOf("저장한곡", "음악파일", "저장앨범")

       override fun onCreateView(
              inflater: LayoutInflater,
              container: ViewGroup?,
              savedInstanceState: Bundle?
       ): View? {
              binding = FragmentLockerBinding.inflate(inflater, container, false)

              val lockerAdapter = LockerVPAdapter(this)
              binding.lockerContentVp.adapter = lockerAdapter
              TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) {
                     tab, position ->
                     tab.text = informaiton[position]
              }.attach()

              binding.lockerLoginTv.setOnClickListener {
                     startActivity(Intent(activity, LoginActivity::class.java))
              }

              return binding.root
       }

       override fun onStart() {
              super.onStart()
              initView()
       }

       private fun getJwt(): Int { // ? 적는 이유: private에서 사용할 때
              val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
              return spf!!.getInt("jwt", 0) // 가져온 값이 없으면 0을 반환
       }

       private fun initView() {
              val jwt : Int = getJwt()
              if (jwt == 0) {
                     binding.lockerLoginTv.text = "로그인"
                     binding.lockerLoginTv.setOnClickListener {
                            startActivity(Intent(activity, LoginActivity::class.java))
                     }
              } else {
                     binding.lockerLoginTv.text = "로그아웃"
                     binding.lockerLoginTv.setOnClickListener {
                            //로그아웃 진행
                            logout()
                            startActivity(Intent(activity, MainActivity::class.java))
                     }
              }
       }

       private fun logout() {
              val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
              val editor = spf!!.edit()
              editor.remove("jwt") //jwt 키 값에 있는거 없앰
              editor.apply()
       }
}