package com.anirecs.AniRecs.database.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MALDatabaseUpdate {
    private MalApiFullFetch malApiFullFetch;

    public MALDatabaseUpdate(MalApiFullFetch malApiFullFetch) {
        this.malApiFullFetch = malApiFullFetch;
    }

    // Get the last page from a file
    public int getLastPage() throws IOException {
        File file = new File("AnimeDbLastPage.txt");
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            return Integer.parseInt(bufferedReader.readLine());
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return 0;
    }

    // Updates the database
    public void updateDatabase() throws IOException {
        int lastPage = getLastPage();

        // Needs to offset it so that it doesn't go to next page which is empty
        lastPage -= 100;

        if (lastPage < 0) {
            lastPage = 0;
        }

        lastPage = malApiFullFetch.getAllData(lastPage);

        writePageToFile(lastPage);
    }

    // Write the last to a new file
    public void writePageToFile(int page) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("AnimeDbLastPage.txt"));
        writer.write(Integer.toString(page));

        writer.close();
    }
}
