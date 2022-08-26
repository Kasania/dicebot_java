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
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoCDice {

    private static final String GOOGLE_SPREADSHEET_PREFIX = "https://docs.google.com/spreadsheets/d/";

    public MessageEmbed command_ruse(@NotNull SlashCommandInteractionEvent event){

        Player player = Player.fromEvent(event);

        List<OptionMapping> message = event.getOptions();
        List<String> args = message.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        String spreadSheetId;
        String sheetName = null;
        int sheetId = -1;
        if(args.get(0).startsWith(GOOGLE_SPREADSHEET_PREFIX)){
            String link = args.get(0).replace(GOOGLE_SPREADSHEET_PREFIX,"");
            String[] data = link.split("/");
            spreadSheetId = data[0];
            //TODO: 멀티프로필 지원
            if(data.length>1){
                sheetId = Integer.parseInt(data[1].split("gid=")[1]);
            }else{
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < args.size(); i++) {
                    stringBuilder.append(args.get(i)).append(" ");
                }
                sheetName = stringBuilder.toString().trim();
            }
        }
        else{
            return SimpleEmbedMessage.titleDescEmbed(":x: 올바르지 않은 시트 링크입니다.",
                    "적절한 시트 링크를 입력해주세요.");
        }

        try {
            if(sheetId == -1){
                System.out.println(sheetName);
                sheetId = SpreadSheetManager.getInstance().getSheetId(spreadSheetId, sheetName);
            }

            SpreadSheetManager.getInstance().forceReloadSheet(spreadSheetId);
        } catch (IOException e) {
            return SimpleEmbedMessage.titleDescEmbed(":x: 올바르지 않은 시트 링크입니다.",
                    "적절한 시트 링크를 입력해주세요.");
        }

        try {

            boolean success = SpreadSheetManager.getInstance().setPlayerSheet(spreadSheetId, sheetId, player);

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

    public MessageEmbed command_rr(@NotNull SlashCommandInteractionEvent event){

        Player player = Player.fromEvent(event);
        WorkSheet sheet = SpreadSheetManager.getInstance().getPlayerSheet(player);

        List<OptionMapping> message = event.getOptions();
        List<String> args = message.stream().map(OptionMapping::getAsString).collect(Collectors.toList());

        String target = args.get(0);
        String expr = "";
        if(args.size()>1){
            expr = args.get(1);
        }

        try{
            int value = sheet.getStat(target);

            String query = "(1d100"+expr+")#"+value;

            DiceResult result = new StackDice().calcExpr(query);
            return SimpleEmbedMessage.diceEmbed(judgement(result, value));

        }catch (IllegalArgumentException e){
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

    public MessageEmbed command_rdel(@NotNull SlashCommandInteractionEvent event){
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

    public MessageEmbed command_rstat(@NotNull SlashCommandInteractionEvent event){
        Player player = Player.fromEvent(event);
        WorkSheet sheet = SpreadSheetManager.getInstance().getPlayerSheet(player);

        if(Objects.isNull(sheet)){
            return SimpleEmbedMessage.titleDescEmbed(":x: 등록된 시트를 발견할 수 없습니다.",
                    "/ruse 명령어를 사용하여 시트를 등록해보세요.");
        }
        else{
            return SimpleEmbedMessage.titleDescEmbed("현재 \"" + sheet.characterName + "\" 탐사자로 플레이하고 있습니다.",
                    "시트 링크",
                    GOOGLE_SPREADSHEET_PREFIX+sheet.spreadSheetID + " # "+sheet.sheetName);
        }
    }

    public MessageEmbed command_rccc(@NotNull SlashCommandInteractionEvent event){

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
