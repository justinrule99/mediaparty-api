package com.mediaparty.api.util;

public class Utils {

    public enum USER_TYPE  {
        ADMIN, PRO, STANDARD
    }

    public enum VIDEO_STATE {
        PAUSED, PLAYING, UNSTARTED, BUFFERING
    }

    public enum SESSION_TYPE {
        YOUTUBE, SPOTIFY
    }

    public enum FRIEND_STATUS {
        PENDING, ACCEPTED
    }

    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

}