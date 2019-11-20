package gte.com.itextmosimayor.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.repository.DepartmentMessagesRepository;

public class DepartmentMessagesViewModel extends AndroidViewModel {

    private LiveData<List<MessagesData>> openMessages;
    private LiveData<List<MessagesData>> importantMessages;

    private DepartmentMessagesRepository repository;

    public DepartmentMessagesViewModel(@NonNull Application application) {
        super(application);
    }

    public void initOpen() {
        repository = DepartmentMessagesRepository.getInstance();
        openMessages = repository.getOpenMessages(getApplication());
    }

    public void initImportant() {
        repository = DepartmentMessagesRepository.getInstance();
        importantMessages = repository.getImportantMessages(getApplication());
    }

    public LiveData<List<MessagesData>> getOpenMessages() {
        return openMessages;
    }

    public LiveData<List<MessagesData>> getImportantMessages() {
        return importantMessages;
    }

}
