package yunho.app.simplechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import yunho.app.simplechat.Adapter.MessageAdapter
import yunho.app.simplechat.DTO.messageDTO
import yunho.app.simplechat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var userDB: DatabaseReference
    private lateinit var chatDB: DatabaseReference
    private lateinit var binding: ActivityMainBinding
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val messageAdapter = MessageAdapter()
        binding.chat.adapter = messageAdapter
        binding.chat.layoutManager = LinearLayoutManager(this)

        if (auth.currentUser != null) {
            Log.e("id", "${auth.uid}")
            binding.login.text = "로그아웃"
        }
        userDB = Firebase.database.reference.child("chat").child("1")
        userDB.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(messageDTO::class.java)
                chatItem ?: return
                messageAdapter.MessageList.add(chatItem)
                messageAdapter.notifyItemInserted(messageAdapter.MessageList.size)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        initSignUpButton()
        initLoginButton()
        initSendButton()
    }

    private fun initSignUpButton() {
        binding.signup.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { Task ->
                    if (Task.isSuccessful) {
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initLoginButton() {
        binding.login.setOnClickListener {
            if (auth.currentUser == null) {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { Task ->
                        if (Task.isSuccessful) {
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                            binding.login.text = "로그아웃"
                        } else {
                            Toast.makeText(this, "실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                auth.signOut()
                binding.login.text = "로그인"
                Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun initSendButton(){
        binding.send.setOnClickListener {
            val senderID = auth.uid.toString()
            val message = binding.messageBar.text.toString()
            val chat = messageDTO(senderID, message)
            userDB.push().setValue(chat)
            binding.messageBar.text.clear()
        }
    }
}