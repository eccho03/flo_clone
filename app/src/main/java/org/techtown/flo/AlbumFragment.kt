package org.techtown.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import org.techtown.flo.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        // Home에서 넘어온 데이터를 반영
        isLiked = isLikedAlbum(album.id)
        setInit(album)
        setClickListeners(album)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
            tab, position ->
            tab.text = information[position]
        }.attach()
        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()

        if (isLiked) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun getJwt(): Int { // ? 적는 이유: private에서 사용할 때
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0) // 가져온 값이 없으면 0을 반환
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Likes(userId, albumId) // 좋아요 눌렀을 때 Like 테이블에 정보를 추가하기 위함

        songDB.AlbumDao().likeAlbum(like)
    }

    //앨범을 눌렀을 때 좋아요를 눌렀는지 확인하기 위한 함수
    private fun isLikedAlbum(albumId: Int) : Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        // 어떤 user가 좋아요..?
        val likeid : Int? = songDB.AlbumDao().isLikedAlbum(userId, albumId)

        return likeid != null
    }

    private fun disLikedAlbum(albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.AlbumDao().disLikedAlbum(userId, albumId)
    }

    private fun setClickListeners(album: Album) {
        val userId = getJwt()
        binding.albumLikeIv.setOnClickListener {
            if (isLiked) {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.id)
            } else {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }
    }


}