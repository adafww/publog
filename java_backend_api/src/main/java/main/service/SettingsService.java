package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.SettingsRequest;
import main.api.response.SettingsResponse;
import main.model.GlobalSettings;
import main.repository.GlobalSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class SettingsService {

    private final GlobalSettingsRepository globalSettingsRepo;

    public SettingsResponse getGlobalSettings(){

        SettingsResponse settingsResponse = new SettingsResponse();
        ArrayList<GlobalSettings> list = (ArrayList<GlobalSettings>) globalSettingsRepo.findAll();
        settingsResponse.setMultiuserMode(list.get(0).getValue().equals("YES"));
        settingsResponse.setPostPremoderation(list.get(1).getValue().equals("YES"));
        settingsResponse.setStatisticIsPublic(list.get(2).getValue().equals("YES"));

        return settingsResponse;
    }

    public void editSettings(SettingsRequest request){

        globalSettingsRepo.updateMultiuserMode(toStringValue(request.isMultiuserMode()));
        globalSettingsRepo.updatePostPremoderation(toStringValue(request.isPostPremoderation()));
        globalSettingsRepo.updateStatisticsIsPublic(toStringValue(request.isStatisticsIsPublic()));

    }

    private String toStringValue(boolean value){

        if(value){
            return "YES";
        }else {
            return "NO";
        }
    }
}
