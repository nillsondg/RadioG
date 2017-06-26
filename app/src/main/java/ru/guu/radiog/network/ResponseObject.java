package ru.guu.radiog.network;

import ru.guu.radiog.network.model.DataModel;

/**
 * Created by dmitry on 22.06.17.
 */

public class ResponseObject<DataType extends DataModel> extends Response {

    private DataType data;

    public DataType getData() {
        return data;
    }
}
