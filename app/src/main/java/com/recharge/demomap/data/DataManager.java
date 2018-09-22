package com.recharge.demomap.data;

import com.recharge.demomap.data.model.user.IUserModel;
import com.recharge.demomap.data.model.user.UserModel;

public class DataManager implements IDataManager {

    public static final String TAG = DataManager.class.getSimpleName();
    

    public DataManager() {
    }

    @Override
    public IUserModel getUserModel () {
        return new UserModel();
    }
}
