package com.helper.west2ol.fzuhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.dto.EmptyRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coder on 2017/9/29.
 */

public class EmptyAdapter extends RecyclerView.Adapter {
    private static final int TYPE_PARENT = 1;
    private static final int TYPE_CHILD = 2;

    private Context context;
    private List<Empty> empties=new ArrayList<>();
    private List<EmptyRoom.DataListBean.DataBean> dataBeenList;

    public EmptyAdapter(Context context,EmptyRoom emptyRoom) {
        super();
        this.context = context;
        dataBeenList=emptyRoom.getDataList().getData();
        String flag = "";
        for (EmptyRoom.DataListBean.DataBean dataBean : dataBeenList) {
            String parent=dataBean.getRoomName().substring(0,2);
            if (flag.equals(parent)) {
                continue;
            }
            Empty empty=new Empty();
            empty.type = TYPE_PARENT;
            empty.roomName=parent;
            empties.add(empty);
            flag=parent;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PARENT) {
            return new EmptyRoomParentHolder(LayoutInflater.from(context).inflate(R.layout.item_empty_room_parent,null));
        }else return new EmptyRoomChildHolder(LayoutInflater.from(context).inflate(R.layout.item_empty_room_child,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Empty empty = empties.get(position);
        switch (empty.type) {
            case TYPE_CHILD:
                EmptyRoomChildHolder childHolder = (EmptyRoomChildHolder) holder;
                childHolder.textView.setText(empty.roomName);
                break;
            case TYPE_PARENT:
                EmptyRoomParentHolder parentHolder = (EmptyRoomParentHolder) holder;
                parentHolder.textView.setText(empty.roomName);
                parentHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String build=empty.getRoomName().substring(0,2);
                        int end=position;
                        if (empty.isOpen) {
                            for (Empty empty : empties) {
                                if (empty.roomName.substring(0,2).equals(build)&&empty.type==TYPE_CHILD){
                                    empties.remove(empty);
                                    end=end+1;
                                }
                            }
                            notifyItemMoved(position, end);
                            empty.isOpen=false;
                        }else {
                            Log.i("EMPTY", position+"");
                            int i = position+1;
                            int count=0;
                            for (Empty empty : empties) {
                                if (empty.roomName.substring(0,2).equals(build)&&empty.type==TYPE_CHILD){
                                    empties.add(i, empty);
                                    i++;
                                    count++;
                                }
                            }
                            notifyItemRangeInserted(position,count);
                            empty.isOpen=true;
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return empties.size();
    }

    @Override
    public int getItemViewType(int position) {
        return empties.get(position).type;
    }

    private class EmptyRoomParentHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public EmptyRoomParentHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_empty_room_parent);
        }
    }

    private class EmptyRoomChildHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public EmptyRoomChildHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_empty_room_child);
        }
    }

    private class Empty{
        int type;
        String roomName;
        boolean isOpen=false;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }
    }

}
