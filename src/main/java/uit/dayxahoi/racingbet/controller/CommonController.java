package uit.dayxahoi.racingbet.controller;

import uit.dayxahoi.racingbet.util.ResourceFile;

public class CommonController {

    private static CommonController instance;

    private CommonController() {

    }

    public static CommonController getInstance() {
        if (instance == null)
            instance = new CommonController();
        return instance;
    }



}
