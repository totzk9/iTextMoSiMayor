package gte.com.itextmosimayor.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.repository.DeletedRepository;

public class DeletedMessagesViewModel extends AndroidViewModel {

    private MutableLiveData<List<MessagesData>> messages;

    public DeletedMessagesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        DeletedRepository repository = DeletedRepository.getInstance();
        messages = repository.getDeletedMessages(getApplication());
    }

    public MutableLiveData<List<MessagesData>> getMessages() {
        return messages;
    }

}
