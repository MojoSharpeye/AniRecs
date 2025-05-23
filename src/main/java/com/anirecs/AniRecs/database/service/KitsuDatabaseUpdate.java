package com.anirecs.AniRecs.database.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class KitsuDatabaseUpdate {
    private KitsuApiFullFetch kitsuApiFullFetch;

    public KitsuDatabaseUpdate(KitsuApiFullFetch kitsuApiFullFetch) {
        this.kitsuApiFullFetch = kitsuApiFullFetch;
    }

    // Get the last page from a file
    public int getLastPage() throws IOException {
        File file = new File("AnimeDbLastPage.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        return Integer.parseInt(bufferedReader.readLine());
    }

    // Updates the database
    public void updateDatabase() throws IOException {
        int lastPage = getLastPage();

        // Needs to offset it so that it doesn't go to next page which is empty
        lastPage -= 40;

        lastPage = kitsuApiFullFetch.getAllData(lastPage);

        writePageToFile(lastPage);
    }

    // Write the last to a new file
    public void writePageToFile(int page) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("AnimeDbLastPage.txt"));
        writer.write(Integer.toString(page));

        writer.close();
    }
}
