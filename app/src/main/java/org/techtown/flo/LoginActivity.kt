package org.techtown.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.techtown.flo.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd : String = binding.loginPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.UserDao().getUser(email, pwd)

        user?.let {
            Log.d("LOGIN_ACT/GET_USER", "userId : ${user.id}, $user")
            saveJwt(user.id)
            startActivity()
        }
    }

    private fun saveJwt(jwt: Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }

    private fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}