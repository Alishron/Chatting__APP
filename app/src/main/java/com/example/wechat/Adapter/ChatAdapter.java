package com.example.wechat.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.Models.MessageModel;
import com.example.wechat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;
    int Sender_view_ttpe = 1 ;
    int Reciever_view_ttpe = 2 ;


    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Sender_view_ttpe){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
      return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
if (messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
    return Sender_view_ttpe;
}
else {return Reciever_view_ttpe;}
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
MessageModel messageModel = messageModels.get(position);

//delete wala optionn-----
holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are you Sure want to delete this message")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                  database.getReference().child("Chats").child(senderRoom).child(messageModel.getMessage())
                          .setValue(null);

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
                    }
                }).show();
        return false;
    }
});

if (holder.getClass() ==SenderViewHolder.class){
    ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
    Date  date =new Date(messageModel.getTimestamp());

    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("h:mm a");
    String strdate = simpleDateFormat.format(date);
    ((SenderViewHolder)holder).SendTime.setText(strdate.toString());

}
else {
    ((RecieverViewHolder)holder).recievermesss.setText(messageModel.getMessage());
    Date  date =new Date(messageModel.getTimestamp());

    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("h:mm a");
    String strdate = simpleDateFormat.format(date);
    ((RecieverViewHolder)holder).recievetime.setText(strdate.toString());
}
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{
        TextView recievermesss , recievetime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recievermesss= itemView.findViewById(R.id.receiverText);
            recievetime= itemView.findViewById(R.id.receiverTime);

        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg , SendTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg= itemView.findViewById(R.id.senderText);
            SendTime= itemView.findViewById(R.id.senderTime);

        }
    }
}
