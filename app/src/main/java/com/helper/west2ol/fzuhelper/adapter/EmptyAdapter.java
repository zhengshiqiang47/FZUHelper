package com.helper.west2ol.fzuhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.dto.Empty;
import com.helper.west2ol.fzuhelper.dto.EmptyRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by coder on 2017/9/29.
 */

public class EmptyAdapter extends RecyclerView.Adapter {
    private static final String TAG = "EmptyAdapter";
    public static final int TYPE_PARENT = 1;
    public static final int TYPE_CHILD = 2;

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<Empty> empties=new ArrayList<>();
    private Map<String,List<Empty>> emptyMap;

    public EmptyAdapter(Context context,Map<String,List<Empty>> emptyMap,RecyclerView recyclerView) {
        super();
        this.context = context;
        this.emptyMap=emptyMap;
        this.recyclerView = recyclerView;
        Iterator iterator=emptyMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Empty>> entry = (Map.Entry<String, List<Empty>>) iterator.next();
            Empty empty = new Empty();
            empty.setType(TYPE_PARENT);
            empty.setRoomName(entry.getKey());
            empty.setOpen(false);
            empties.add(empty);
        }
        Collections.sort(empties, new Comparator<Empty>() {
            @Override
            public int compare(Empty o1, Empty o2) {
                int r1=Integer.parseInt(o1.getRoomName().replaceAll("([0-9]*)\\-([0-9]*)","$1"));
                int r2=Integer.parseInt(o2.getRoomName().replaceAll("([0-9]*)\\-([0-9]*)","$1"));
                int x1 = Integer.parseInt(o1.getRoomName().replaceAll("([0-9]*)\\-([0-9]*)","$2"));
                int x2 = Integer.parseInt(o2.getRoomName().replaceAll("([0-9]*)\\-([0-9]*)","$2"));
                if (x1 - r1 > x2 - r2) {
                    return 1;
                } else if (x1 - r1 == x2 - r2) {
                    if (r1 > r2) {
                        return 1;
                    } else if (r1 == r2) {

                        return 0;
                    }else {
                        return -1;
                    }
                }else {
                    return -1;
                }

            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PARENT) {
            return new EmptyRoomParentHolder(LayoutInflater.from(context).inflate(R.layout.item_empty_room_parent,null));
        }else return new EmptyRoomChildHolder(LayoutInflater.from(context).inflate(R.layout.item_empty_room_child,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int pos) {
        Log.i(TAG, "onBindViewHolder: "+pos);
        final Empty empty = empties.get(pos);
        switch (empty.type) {
            case TYPE_PARENT:
                final EmptyRoomParentHolder parentHolder = (EmptyRoomParentHolder) holder;
                TextView textView=parentHolder.textView;
                TextView countView=parentHolder.countView;
                RelativeLayout layout=parentHolder.layout;
                final ImageView arrowIcon=parentHolder.arrowIcon;
                if (empty.isOpen) {
                    arrowIcon.setRotation(90);
                }else arrowIcon.setRotation(0);
                textView.setText(empty.roomName+" 节");
                countView.setText("共 "+emptyMap.get(empty.getRoomName()).size()+" 间");
                layout.setTag(pos);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position= (int) view.getTag();
                        if (position>=empties.size()){
                            notifyDataSetChanged();
                            Log.i(TAG, "onClick: here "+position);
                            return;
                        }
                        final Empty empty = empties.get(position);
                        float centerX = arrowIcon.getPivotX();
                        float centerY = arrowIcon.getPivotY();
//                        float sRotation=0;
//                        Log.i(TAG, "onClick: rotaion" + rotaition);
                        RotateAnimation rotateAnimation =null;
                        if (empty.isOpen) {//收缩
                            //箭头动画
                            if (arrowIcon.getRotation() >= 90) {
                                rotateAnimation = new RotateAnimation(0, 0-90,centerX,centerY);
                            }else if (arrowIcon.getRotation()>=0){
                                rotateAnimation = new RotateAnimation(90, 0,centerX,centerY);
                            }
                            List<Empty> rmEmpties=emptyMap.get(empty.getRoomName());
                            empties.removeAll(rmEmpties);
                            empty.setOpen(false);
                            empty.setRotation(0);
                            notifyItemRangeRemoved(position+1,rmEmpties.size());
                            notifyItemRangeChanged(position+1,empties.size());
//                            Log.i(TAG, " onClick:"+empties.size()+" position:"+position+" roomName:"+empty.getRoomName());
                        }else {//展开
                            if (arrowIcon.getRotation() >= 90) {
                                rotateAnimation = new RotateAnimation(-90, 0,centerX,centerY);
                            }else if (arrowIcon.getRotation()>=0){
                                rotateAnimation = new RotateAnimation(0, 90,centerX,centerY);
                            }
                            List<Empty> rmEmpties=emptyMap.get(empty.getRoomName());
                            if (rmEmpties == null) {
                                return;
                            }
                            for (Empty e : rmEmpties) {
                                e.setType(TYPE_CHILD);
                            }
//                            Log.i(TAG, "onClick: "+empties.size()+" position:"+position+" roomName"+empty.getRoomName());
                            empties.addAll(position+1,rmEmpties);
                            empty.isOpen=true;
                            empty.setRotation(90);
                            notifyItemRangeInserted(position+1,rmEmpties.size());
                            notifyItemRangeChanged(position+1,empties.size());
                        }
                        rotateAnimation.setDuration(300l);
                        rotateAnimation.setFillAfter(true);
                        rotateAnimation.setInterpolator(new DecelerateInterpolator());
//                        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                arrowIcon.setRotation(empty.getRotation());
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//
//                            }
//                        });
                        arrowIcon.startAnimation(rotateAnimation);
                    }
                });
                break;

            case TYPE_CHILD:
                EmptyRoomChildHolder childHolder = (EmptyRoomChildHolder) holder;
                childHolder.textView.setText(empty.roomName.replaceAll(" ([0-9]*)\\(([0-9]*)\\)"," 可容纳 $1 人 "));
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
        RelativeLayout layout;
        ImageView arrowIcon;
        TextView countView;

        public EmptyRoomParentHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_empty_room_parent);
            layout = (RelativeLayout) itemView.findViewById(R.id.item_empty_room_parent_layout);
            arrowIcon = (ImageView) itemView.findViewById(R.id.item_empty_room_icon);
            countView = (TextView) itemView.findViewById(R.id.item_empty_room_parent_count);
        }
    }

    private class EmptyRoomChildHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public EmptyRoomChildHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_empty_room_child);
        }
    }

}
