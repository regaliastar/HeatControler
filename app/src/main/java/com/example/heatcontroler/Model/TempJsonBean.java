package com.example.heatcontroler.Model;

import java.util.List;

/**
 * Created by 艾德米 on 2018/6/2.
 */

public class TempJsonBean {


    /**
     * errno : 0
     * data : {"count":2,"datastreams":[{"datapoints":[{"at":"2018-05-19 09:28:27.004","value":"55"}],"id":"expectedTemperature"},{"datapoints":[{"at":"2018-06-02 16:18:51.884","value":250}],"id":"temperature"}]}
     * error : succ
     */

    public int errno;
    public DataBean data;
    public String error;

    public static class DataBean {
        /**
         * count : 2
         * datastreams : [{"datapoints":[{"at":"2018-05-19 09:28:27.004","value":"55"}],"id":"expectedTemperature"},{"datapoints":[{"at":"2018-06-02 16:18:51.884","value":250}],"id":"temperature"}]
         */

        public int count;
        public List<DatastreamsBean> datastreams;

        public static class DatastreamsBean {
            /**
             * datapoints : [{"at":"2018-05-19 09:28:27.004","value":"55"}]
             * id : expectedTemperature
             */

            public String id;
            public List<DatapointsBean> datapoints;

            public static class DatapointsBean {
                /**
                 * at : 2018-05-19 09:28:27.004
                 * value : 55
                 */

                public String at;
                public String value;
            }
        }
    }
}
