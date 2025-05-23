package com.anirecs.AniRecs.controller;

import com.anirecs.AniRecs.database.service.KitsuApiFullFetch;
import com.anirecs.AniRecs.database.service.KitsuDatabaseUpdate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DatabaseController {
    KitsuApiFullFetch fullFetch;

    KitsuDatabaseUpdate databaseUpdate;

    public DatabaseController(KitsuApiFullFetch kitsuApiFullFetch, KitsuDatabaseUpdate databaseUpdate) {
        this.fullFetch = kitsuApiFullFetch;
        this.databaseUpdate = databaseUpdate;
    }

    @RequestMapping("/database/reset")
    public void resetDatabase() {
        fullFetch.getAllData(0);
    }

    @RequestMapping("/database/update")
    public void updateDatabase() throws IOException {
        databaseUpdate.updateDatabase();
    }
}
