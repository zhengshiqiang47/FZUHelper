package com.helper.west2ol.fzuhelper.dto;

import java.util.List;

/**
 * Created by coder on 2017/9/29.
 */

public class EmptyRoom {


    /**
     * dataList : {"data":[{"roomName":"西1-105 121(71) 多媒体"},{"roomName":"西1-204 38(19) 多媒体"},{"roomName":"西1-301 153(92) 多媒体"},{"roomName":"西1-402 125(73) 多媒体"},{"roomName":"西1-403 123(71) 多媒体"},{"roomName":"西1-405 121(71) 多媒体"},{"roomName":"西1-406 135(79) 多媒体"},{"roomName":"西1-505 121(71) 多媒体"},{"roomName":"查询于2017/09/29 23:07"}]}
     */

    private DataListBean dataList;

    public DataListBean getDataList() {
        return dataList;
    }

    public void setDataList(DataListBean dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * roomName : 西1-105 121(71) 多媒体
             */

            private String roomName;

            public String getRoomName() {
                return roomName;
            }

            public void setRoomName(String roomName) {
                this.roomName = roomName;
            }
        }
    }
}
