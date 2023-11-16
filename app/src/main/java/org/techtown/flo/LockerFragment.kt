package org.techtown.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.techtown.flo.databinding.FragmentLockerBinding

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
              //binding.lockerContentVp.adapter = lockerAdapter(this)
              return binding.root
       }
}