package com.chili_driver.interfaces;


public interface Listeners {



    interface ProfileActions
    {



    }
    interface SignUpListener {

        void openSheet();

        void closeSheet();

        void checkDataValid();

        void checkReadPermission();

        void checkCameraPermission();
    }



}
