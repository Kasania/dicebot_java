/*
 * Author : Kasania
 * Filename : CoCDice
 * Desc :
 */
package com.kasania.dicebot.v1;

import com.kasania.dicebot.common.SimpleEmbedMessage;
import com.kasania.dicebot.common.SpreadSheetManager;
import com.kasania.dicebot.common.WorkSheet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class CoCDice {

    private static final String GOOGLE_SPREADSHEET_PREFIX = "https://docs.google.com/spreadsheets/d/";

    public MessageEmbed command_ruse(@NotNull MessageReceivedEvent event){

        Player player = Player.fromEvent(event);

        String message = event.getMessage().getContentDisplay();
        String[] args = message.split(" ");
        String sheetID;
        if(args[1].startsWith(GOOGLE_SPREADSHEET_PREFIX)){
            String link = args[1].replace(GOOGLE_SPREADSHEET_PREFIX,"");
            sheetID = link.split("/")[0];
        }
        else{
            return SimpleEmbedMessage.titleDescEmbed(":x: 올바르지 않은 시트 링크입니다.",
                    "적절한 시트 링크를 입력해주세요.");
        }

        if(args.length<3){
            return SimpleEmbedMessage.titleDescEmbed(":x: 시트 이름이 누락되었습니다.",
                    "적절한 시트 이름을 입력해주세요.");
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 2; i < args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }
        String sheetName = stringBuilder.toString().trim();

        try {
            SpreadSheetManager.getInstance().forceReloadSheet(sheetID);
        } catch (IOException e) {
            return SimpleEmbedMessage.titleDescEmbed(":x: 올바르지 않은 시트 링크입니다.",
                    "적절한 시트 링크를 입력해주세요.");
        }

        try {

            boolean success = SpreadSheetManager.getInstance().setPlayerSheet(sheetID, sheetName, player);

            if(!success){
                return SimpleEmbedMessage.titleDescEmbed(":x: 시트를 인식하는데 실패했습니다.",
                        "적절한 시트 이름 또는 형식을 입력해주세요.");
            }

        } catch (FileNotFoundException e) {
            return SimpleEmbedMessage.titleDescEmbed(":x: 올바르지 않은 시트 링크입니다.",
                    "적절한 시트 링크를 입력해주세요.");
        }
        catch (NumberFormatException e) {
            return SimpleEmbedMessage.titleDescEmbed(":x: 시트의 데이터가 잘못되었습니다.",e.getMessage());
        }

        WorkSheet sheet = SpreadSheetManager.getInstance().getPlayerSheet(player);
        return SimpleEmbedMessage.titleDescEmbed(":o: 시트 등록 성공!",
                                "지금부터 "+sheet.characterName + " 탐사자로 플레이합니다.");
    }

    public MessageEmbed command_rr(@NotNull MessageReceivedEvent event){

        Player player = Player.fromEvent(event);
        WorkSheet sheet = SpreadSheetManager.getInstance().getPlayerSheet(player);

        String message = event.getMessage().getContentDisplay();
        String[] args = message.split(" ");

        String target = args[1];
        String expr = "";
        if(args.length>2){
            expr = args[2];
        }

        try{
            int value = sheet.getStat(target);

            String query = "(1d100"+expr+")#"+value;

            DiceResult result = new StackDice().calcExpr(query);
            return SimpleEmbedMessage.diceEmbed(judgement(result, value));

        }catch (IllegalArgumentException e){
            System.out.println(e);
            return SimpleEmbedMessage.titleDescEmbed(":x: 해당 이름의 특성/기능이 없습니다.",
                    "명령어를 확인해주세요.");
        }
    }

    private DiceResult judgement(DiceResult result, int target){
        int diceValue = Integer.parseInt(result.judgements.get(0).split("<=")[0]);

        if(diceValue == 100){
            result.result = "100!!!";
        }
        else if(diceValue > target){
            if(target < 50 && diceValue >= 96){
                result.result = "대실패";
            }
            else{
                result.result = "실패";
            }
        }
        else if(target / 2 < diceValue ){
            result.result = "성공";
        }
        else if(target / 5 < diceValue ){
            result.result = "어려운 성공";
        }
        else if(target / 5 >= diceValue ){
            if(diceValue == 1){
                result.result = "1!!!";
            }
            else{
                result.result = "극단적 성공";
            }
        }

        return result;
    }

    public MessageEmbed command_rdel(@NotNull MessageReceivedEvent event){
        Player player = Player.fromEvent(event);
        WorkSheet workSheet = SpreadSheetManager.getInstance().removePlayerSheet(player);

        if(Objects.isNull(workSheet)){
            return SimpleEmbedMessage.titleDescEmbed(":x: 시트 해제 실패!", "등록된 시트를 발견할 수 없습니다.");
        }
        else{
            return SimpleEmbedMessage.titleDescEmbed(":o: 시트 해제 성공!",
                    "지금부터 "+workSheet.sheetName + " 시트를 사용하지 않습니다.");
        }

    }

    public MessageEmbed command_rstat(@NotNull MessageReceivedEvent event){
        Player player = Player.fromEvent(event);
        WorkSheet sheet = SpreadSheetManager.getInstance().getPlayerSheet(player);

        if(Objects.isNull(sheet)){
            return SimpleEmbedMessage.titleDescEmbed(":x: 등록된 시트를 발견할 수 없습니다.",
                    "!ruse 명령어를 사용하여 시트를 등록해보세요.");
        }
        else{
            return SimpleEmbedMessage.titleDescEmbed("현재 \"" + sheet.characterName + "\" 탐사자로 플레이하고 있습니다.",
                    "시트 링크",
                    GOOGLE_SPREADSHEET_PREFIX+sheet.sheetID + " # "+sheet.sheetName);
        }
    }

    public MessageEmbed command_rccc(@NotNull MessageReceivedEvent event){

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":game_die: CoC 7th 캐릭터 메이킹");
        StackDice stackDice = new StackDice();

        DiceResult[] diceResults = new DiceResult[9];

        for (int i = 0; i < 5; i++) {
            diceResults[i] = stackDice.calcExpr("3d6*5");
        }

        for (int i = 5; i < 8; i++) {
            diceResults[i] = stackDice.calcExpr("(2d6+6)*5");
        }
        diceResults[8] = stackDice.calcExpr("3d6*5");

        StringBuilder diceValue = new StringBuilder();
        int i = 0;
        for (String s : new String[]{"근력", "민첩", "정신", "건강", "외모", "교육", "크기", "지능", "행운"}) {
            for (DiceBunch diceBunch : diceResults[i].diceBunches) {
                diceValue.append("[");
                for (String dice : diceBunch.dices) {
                    diceValue.append(dice).append(", ");
                }
                diceValue.append(diceBunch.query).append("]\n");
            }

            embed.addField(s,diceResults[i++].result,true);
        }

        embed.addField("주사위 목록", diceValue.toString(),true);

        return embed.build();
    }

}
