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
import com.kosa.gather_e.DBKey.Companion.DB_CHATS
import com.kosa.gather_e.R
import com.kosa.gather_e.model.entity.chat.ChatListItem
import com.kosa.gather_e.databinding.FragmentChatListBinding
import com.kosa.gather_e.model.entity.user.CurrUser
import com.kosa.gather_e.ui.chatdetail.ChatRoomActivity


class ChatListFragment : Fragment(R.layout.fragment_chat_list) {

    private var binding: FragmentChatListBinding? = null
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()

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
                intent.putExtra("userId", chatRoom.userId)
                intent.putExtra("gatherTitle", chatRoom.gatherTitle)
                intent.putExtra("gatherDate", chatRoom.gatherDate)
                intent.putExtra("gatherLimit", chatRoom.gatherLimit)
                intent.putExtra("gatherCategory", chatRoom.gatherCategory)
                intent.putExtra("gatherCategorySeq", chatRoom.gatherCategorySeq)
                intent.putExtra("gatherParticipants", ArrayList(chatRoom.participants))
                intent.putExtra("gatherParticipantTokens", ArrayList(chatRoom.participantTokens))
                intent.putExtra("gatherPlace", chatRoom.gatherPlace)
                startActivity(intent)
            }
        })

        chatRoomList.clear()

        fragmentChatlistBinding.chatListRecyclerView.adapter = chatListAdapter
        fragmentChatlistBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        val chatDB = Firebase.database.reference.child(DB_CHATS)

        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return
                    if (model.participants.contains(CurrUser.getUserName())) {
                        chatRoomList.add(model)
                    }

//                    chatRoomList.add(model)
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