package org.techtown.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragmentlist.size // 한 줄의 코드만 작성할 때

    override fun createFragment(position: Int): Fragment = fragmentlist[position] // 0,1,2,3

    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment) // fragment를 추가해준다
        notifyItemInserted(fragmentlist.size-1) // 새로운 값이 list에 추가됐음을 알려주기 위해
    }

}