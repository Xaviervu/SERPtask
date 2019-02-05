package ru.vegax.xavier.a3test.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import ru.vegax.xavier.a3test.models.User;
import ru.vegax.xavier.a3test.models.UserWrapper;
import ru.vegax.xavier.a3test.repositories.UsersRepository;

class MainActivityViewModel extends ViewModel {

    private MutableLiveData<UserWrapper> mUsers;

    void init(){
        if(mUsers != null){
            return;
        }
        UsersRepository repo = UsersRepository.getInstance();
        mUsers = repo.getUsers();
    }

    @NonNull LiveData<UserWrapper> getUsers() {
        return mUsers;
    }

}
