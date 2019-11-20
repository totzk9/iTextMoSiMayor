package gte.com.itextmosimayor.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.repository.ResolvedRepository;

public class ResolvedMessagesViewModel extends AndroidViewModel {

    private MutableLiveData<List<MessagesData>> messages;

    public ResolvedMessagesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        ResolvedRepository repository = ResolvedRepository.getInstance();
        messages = repository.getResolvedMessages(getApplication());
    }

    public void initByDepartment() {
        ResolvedRepository repository = ResolvedRepository.getInstance();
        messages = repository.getDepartmentResolvedMessages(getApplication());
    }

    public MutableLiveData<List<MessagesData>> getResolvedMessages() {
        return messages;
    }

    public MutableLiveData<List<MessagesData>> getDepartmentResolvedMessage() {
        return messages;
    }

}
