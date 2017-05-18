package io.euphoria.xkcd.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import io.euphoria.xkcd.app.control.RoomController;
import io.euphoria.xkcd.app.impl.ui.RoomUIManagerImpl;
import io.euphoria.xkcd.app.ui.RoomUIManager;

/**
 * @author N00bySumairu
 */

public class RoomControllerFragment extends Fragment {

    RoomUIManager roomUIManager;
    RoomController roomController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomUIManager = new RoomUIManagerImpl();
        roomController = new RoomController(roomUIManager, getActivity().getApplicationContext());
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        roomController.shutdown();
    }

    public RoomController getRoomController() {
        return roomController;
    }
}
