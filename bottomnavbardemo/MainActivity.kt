package com.example.bottomnavbardemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.bottomnavbardemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val uploadFragment = UploadFragment(this)
        val favoriteFragment = FavoriteFragment()
        setFragmentView(homeFragment)
        //Set the navigation view
        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home ->  setFragmentView(homeFragment)
                R.id.nav_upload -> setFragmentView(uploadFragment)
                R.id.nav_favorites -> setFragmentView(favoriteFragment)
            }
            true
        }
    }

    private  fun setFragmentView(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_fragment_view, fragment)
            //Will return to previous page when tap "Back Button" on the phone
            addToBackStack(null)
            commit()
        }
    }
}