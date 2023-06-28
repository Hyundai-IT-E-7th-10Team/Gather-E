package com.kosa.gather_e.ui.chatlist
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kosa.gather_e.R
import com.kosa.gather_e.data.model.chat.ChatListItem
import com.kosa.gather_e.databinding.FragmentChatListBinding
import com.kosa.gather_e.ui.chatdetail.ChatRoomActivity


class ChatListFragment : Fragment(R.layout.fragment_chat_list) {

    private var binding: FragmentChatListBinding? = null
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()

    private val user: String = "user01"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatlistBinding = FragmentChatListBinding.bind(view)
        binding = fragmentChatlistBinding
        Log.d("Gahter_E","move")


        chatListAdapter = ChatListAdapter(onItemClicked = { chatRoom ->
            // 채팅방으로 이동 하는 코드
            context?.let {
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatRoom.key)
                startActivity(intent)
            }
        })

        chatRoomList.clear()

        fragmentChatlistBinding.chatListRecyclerView.adapter = chatListAdapter
        fragmentChatlistBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)


        if (user == null) {
            return
        }

        val chatDB = Firebase.database.reference.child("Chats")

        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                    Log.d("Gahter_E",chatRoomList.toString())
                }

                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }


    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()
    }
}