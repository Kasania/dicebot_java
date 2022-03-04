/*
 * Author : Kasania
 * Filename : SpreadSheetManager
 * Desc :
 */
package com.kasania.dicebot.common;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.kasania.dicebot.v1.Player;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.*;

@Slf4j
public class SpreadSheetManager {

    private static SpreadSheetManager INSTANCE;

    private final Map<Player, WorkSheet> PLAYER_SHEETS;

    private Sheets sheetsService;

    public static SpreadSheetManager getInstance(){
        if(Objects.isNull(INSTANCE)){
            INSTANCE = new SpreadSheetManager("dicebot-key.json");
        }
        return INSTANCE;
    }


    private SpreadSheetManager(String credentialPath){
        PLAYER_SHEETS = Collections.synchronizedMap(new HashMap<>());

        //TODO: LOAD
        try {
            GsonFactory gsonFactory = GsonFactory.getDefaultInstance();
            log.info("Start SpreadSheetManager");
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            InputStream in = Files.newInputStream(Path.of(credentialPath));
            ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(in);

            log.info("Start SheetsService");
            sheetsService = new Sheets.Builder(HTTP_TRANSPORT, gsonFactory,new HttpCredentialsAdapter(credentials))
                    .setApplicationName("DiceBot")
                    .build();

        } catch (IOException | GeneralSecurityException e) {
            log.error("{}",e.getMessage(),e);
        }
    }

    public WorkSheet getPlayerSheet(Player player){
        try {
            return reloadSheet(player);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return PLAYER_SHEETS.get(player);
    }

    public boolean setPlayerSheet(String sheetID, String sheetName, Player player) throws FileNotFoundException, NumberFormatException {

        WorkSheet workSheet;

//        if(Objects.isNull(workSheet)){
            try {
                for (Sheet sheet : sheetsService.spreadsheets().get(sheetID).execute().getSheets()) {
                    if(sheet.getProperties().getTitle().equals(sheetName)){
                        ValueRange valueRange = sheetsService.spreadsheets().values().get(sheetID,sheetName).execute();
                        workSheet = new WorkSheet(sheetID,sheetName, valueRange);
                        PLAYER_SHEETS.put(player,workSheet);
                        return true;
                    }
                }
            } catch (IOException e) {
                throw new FileNotFoundException("Invalid SpreadSheet ID");
            }
//        }
        return false;
    }

    public WorkSheet reloadSheet(Player player) throws FileNotFoundException {
        WorkSheet workSheet = PLAYER_SHEETS.get(player);
        setPlayerSheet(workSheet.sheetID, workSheet.sheetName, player);
        return PLAYER_SHEETS.get(player);
    }

    public WorkSheet removePlayerSheet(Player player){
        return PLAYER_SHEETS.remove(player);
    }

}
