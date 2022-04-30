package yunho.app.simplechat.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import yunho.app.simplechat.DTO.messageDTO
import yunho.app.simplechat.R

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    val MessageList = mutableListOf<messageDTO>()
    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        fun bind(messageDTO: messageDTO){
            itemView.findViewById<TextView>(R.id.userID).text = messageDTO.senderID
            itemView.findViewById<TextView>(R.id.message).text = messageDTO.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_message,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(MessageList[position])
    }

    override fun getItemCount(): Int {
        return MessageList.size
    }
}