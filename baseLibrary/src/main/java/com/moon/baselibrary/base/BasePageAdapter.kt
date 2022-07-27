package com.moon.baselibrary.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class BasePageAdapter(manager: FragmentManager):FragmentPagerAdapter(manager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
   private  var fragments:ArrayList<Fragment> = ArrayList()
   private  var titles:ArrayList<String> = ArrayList()


    override fun getCount(): Int =fragments.size?:0

    override fun getItem(position: Int): Fragment =fragments[position]

    /*override fun getPageTitle(position: Int): CharSequence =titles[position]*/

    public fun addFragment(fragment: Fragment,title:String){
        fragments.add(fragment)
        titles.add(title)
    }


}