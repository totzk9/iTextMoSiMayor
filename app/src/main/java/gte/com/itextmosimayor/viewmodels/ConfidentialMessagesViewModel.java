package gte.com.itextmosimayor.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.repository.ConfidentialRepository;

public class ConfidentialMessagesViewModel extends AndroidViewModel {

    private MutableLiveData<List<MessagesData>> messages;

    public ConfidentialMessagesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        ConfidentialRepository repository = ConfidentialRepository.getInstance();
        messages = repository.getConfidentialMessages(getApplication());
    }

    public MutableLiveData<List<MessagesData>> getMessages() {
        return messages;
    }

}
