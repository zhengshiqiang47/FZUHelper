package com.helper.west2ol.fzuhelper.dto;

/**
 * Created by coder on 2017/10/8.
 */

public  class Empty{
    public int type;
    public String roomName;
    public boolean isOpen=false;
    public float rotation;
//        @Override
//        public boolean equals(Object obj) {
//            Empty empty = (Empty) obj;
//            if (empty.getId() == id) {
//                return true;
//            }else {
//                return false;
//            }
//        }



    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

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