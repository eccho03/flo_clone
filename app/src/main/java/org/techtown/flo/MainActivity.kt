package org.techtown.flo

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import org.techtown.flo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var song : Song = Song()
    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

        binding.mainPlayerCl.setOnClickListener {
            //intent: 하나의 activity에서 사용하는 택배 상자
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
            startActivity(intent)
            //2주차 미션
            //Toast.makeText(this.applicationContext, song.title, Toast.LENGTH_SHORT).show()
            //getResultText.launch(intent)
        }
    }

    // 2주차 미션
//    companion object { const val STRING_INTENT_KEY = "my_string_key"}
//
//    private val getResultText = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val returnString =
//                result.data?.getStringExtra(STRING_INTENT_KEY)
//        }
//
//    }
        private fun initBottomNavigation(){

            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()

            binding.mainBnv.setOnItemSelectedListener{ item ->
                when (item.itemId) {

                    R.id.homeFragment -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, HomeFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }

                    R.id.lookFragment -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, LookFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                    R.id.searchFragment -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, SearchFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                    R.id.lockerFragment -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, LockerFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                }
                false
            }
        }

    private fun setMiniPlayer(song : Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime
    }

    // 액티비티 전환 시 onStart부터 시작됨 (화면에 표시되기 직전)
    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if (songJson == null) {
            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
        } else {
            gson.fromJson(songJson, Song::class.java)
        }

        setMiniPlayer(song)

    }


}