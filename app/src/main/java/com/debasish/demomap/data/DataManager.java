package com.debasish.demomap.data;

import com.debasish.demomap.data.model.user.IUserModel;
import com.debasish.demomap.data.model.user.UserModel;

public class DataManager implements IDataManager {

    public static final String TAG = DataManager.class.getSimpleName();
    

    public DataManager() {
    }

    @Override
    public IUserModel getUserModel () {
        return new UserModel();
    }
}
