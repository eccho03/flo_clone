package org.techtown.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.techtown.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity(){

    lateinit var binding : ActivitySongBinding
    lateinit var song : Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null //Activity가 소멸될 때 media player를 해제시켜주어야 함
    private var gson: Gson = Gson()

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
//        initClickListener()
//        setPlayer(song)
//
//        //재생버튼
//        binding.songMiniplayerIv.setOnClickListener {
//            //주석은 2주차 미션
////            val intent = Intent(this, MainActivity::class.java).apply {
////                putExtra(MainActivity.STRING_INTENT_KEY, binding.songMusicTitleTv.text)
////            }
//            setResult(Activity.RESULT_OK, intent)
//            if (isFinishing) finish() // Activity가 종료되었는지 여부 판단할 때 사용되는 값
//            // 현재 코드에서는 아직 activity가 종료가 되지 않았으니 !isFinishing이어서 finish() 호출됨
//            setPlayerStatus(true)
//        }
    }

    // 사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        // 내부 저장소에 데이터를 저장할 수 있음. 앱이 실행되다가 종료돼도 꺼내서 다시 사용할 수 있도록 함
        val editor = sharedPreferences.edit() // 에디터

        /*
        editor.putString("title", song.title)
        editor.putString("singer", song.singer)
        .
        .
        .
        */
        //위에거를 간단하게 해주는게 json임. gson은 자바 객체<=>json 변환을 간단하게 해줌
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        //* 꼭 호출되어야 하는 부분*(그래야 실제 저장공간에 저장됨)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }


    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song (
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName) // 현재 받은게 string 값이므로 이 코드를 통해서 resource를 받아준다,
        mediaPlayer = MediaPlayer.create(this, music) //이 음악을 재생할거야

        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying : Boolean) {

        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) { //media player는 재생중이 아닐 때 pause하면 오류 발생 가능
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int,var isPlaying: Boolean = true):Thread(){

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true){

                    if (second >= playTime){
                        break
                    }

                    if (isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second / 60, second % 60)
                            }
                            second++
                        }

                    }

                }

            }catch (e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }

}