package gte.com.itextmosimayor.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.repository.MessagesRepository;

public class MessagesViewModel extends AndroidViewModel {

    private LiveData<List<MessagesData>> unassignedMessages;
    private LiveData<List<MessagesData>> openMessages;
    private LiveData<List<MessagesData>> importantMessages;
    private MessagesRepository repository;

    public MessagesViewModel(@NonNull Application application) {
        super(application);
    }

    public void initUnassigned() {
        repository = MessagesRepository.getInstance();
        unassignedMessages = repository.getUnassignedMessages(getApplication());

    }

    public void initOpen() {
        repository = MessagesRepository.getInstance();
        openMessages = repository.getOpenMessages(getApplication());

    }

    public void initImportant() {
        repository = MessagesRepository.getInstance();
        importantMessages = repository.getImportantMessages(getApplication());

    }

    public LiveData<List<MessagesData>> getUnassignedMessages() {
        return unassignedMessages;
    }

    public LiveData<List<MessagesData>> getOpenMessages() {
        return openMessages;
    }

    public LiveData<List<MessagesData>> getImportantMessages() {
        return importantMessages;
    }

}
