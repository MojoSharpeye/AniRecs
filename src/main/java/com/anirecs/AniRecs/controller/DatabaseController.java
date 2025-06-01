package com.anirecs.AniRecs.controller;

import com.anirecs.AniRecs.database.service.MalApiFullFetch;
import com.anirecs.AniRecs.database.service.MALDatabaseUpdate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DatabaseController {
    MalApiFullFetch fullFetch;

    MALDatabaseUpdate databaseUpdate;

    public DatabaseController(MalApiFullFetch malApiFullFetch, MALDatabaseUpdate databaseUpdate) {
        this.fullFetch = malApiFullFetch;
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
