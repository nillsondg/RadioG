package ru.guu.radiog.network;

import java.util.ArrayList;

import ru.guu.radiog.network.model.DataModel;

/**
 * Created by dmitry on 22.06.17.
 */
public class ResponseArray<DataType extends DataModel> extends Response {

    private ArrayList<DataType> data;

    public ArrayList<DataType> getData() {
        return data;
    }
}