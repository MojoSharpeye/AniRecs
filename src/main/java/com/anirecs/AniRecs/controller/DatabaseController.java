package com.anirecs.AniRecs.controller;

import com.anirecs.AniRecs.database.service.KitsuApiFullFetch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {
    KitsuApiFullFetch fullFetch;

    public DatabaseController(KitsuApiFullFetch kitsuApiFullFetch) {
        this.fullFetch = kitsuApiFullFetch;
    }

    @RequestMapping("/database/reset")
    public void resetDatabase() {
        fullFetch.getAllData();
    }

    @RequestMapping("/database/update")
    public void updateDatabase(){

    }
}
