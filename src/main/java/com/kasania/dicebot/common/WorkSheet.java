/*
 * Author : Kasania
 * Filename : WorkSheet
 * Desc :
 */
package com.kasania.dicebot.common;

import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorkSheet {

    public final String spreadSheetID;
    public final int sheetID;
    public final String sheetName;

    public final String characterName;
    private final Map<String, Integer> skills;
    private final Map<String, Integer> traits;

    private final List<List<Object>> values;

    private final Map<String,String> fixPos = Map.of(
            "근력", "7:15",
            "민첩", "7:19",
            "정신", "7:23",
            "건강", "9:15",
            "외모", "9:19",
            "교육", "9:23",
            "크기", "11:15",
            "지능", "11:19",
            "행운", "18:2",
            "이성", "15:21");


    public WorkSheet(String spreadSheetID, int sheetID, String sheetName, ValueRange valueRange) throws NumberFormatException{

        traits = new HashMap<>();
        skills = new HashMap<>();

        this.spreadSheetID = spreadSheetID;
        this.sheetName = sheetName;
        this.sheetID = sheetID;
        values = valueRange.getValues();
        characterName = values.get(6).get(4).toString();

        addTraits();
        addSkills();
    }

    private void addTraits() throws NumberFormatException {
        for (Map.Entry<String, String> trait : fixPos.entrySet()) {
            String[] value = trait.getValue().split(":");
            int row = Integer.parseInt(value[0])-1;
            int col = Integer.parseInt(value[1]);
            try{
                traits.put(trait.getKey(),Integer.parseInt(values.get(row).get(col).toString()));
            }catch (NumberFormatException e){
                throw new NumberFormatException("Invalid data on trait: "+trait.getKey()+" row("+row+")col("+col+")");
            }

        }

    }

    private void addSkills() throws NumberFormatException{
        //row : 25~43
        //col : 3, 11, 19
        for (int col : new int[]{3, 11, 19}) {
            for (int row = 25; row < 44; row++) {
                try{
                    String skillName = values.get(row).get(col).toString();
                    if(!skillName.equals("")){
                        String skillValue = values.get(row).get(col+4).toString().trim();
                        if(!skillValue.equals("")){
                            try{
                                int skillNum = Integer.parseInt(skillValue);
                                skills.put(skillName,skillNum);
                            }catch (NumberFormatException e){
                                throw new NumberFormatException("Invalid data on skill: "+skillName+" row("+(row+1)+"), col("+(col+4)+")");
                            }

                        }
                    }
                }catch (IndexOutOfBoundsException ignored){
                }
            }
        }
    }

    public int getStat(String name){
        if(traits.containsKey(name)){
            return traits.get(name);
        }
        else if(skills.containsKey(name)){
            return skills.get(name);
        }
        throw new IllegalArgumentException("There is no Stat of "+ name);
    }

    public Map<String,Integer> getAllStat(){
        Map<String, Integer> stats = new HashMap<>();
        stats.putAll(traits);
        stats.putAll(skills);
        return stats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkSheet workSheet = (WorkSheet) o;
        return sheetID == workSheet.sheetID && spreadSheetID.equals(workSheet.spreadSheetID) && sheetName.equals(workSheet.sheetName) && characterName.equals(workSheet.characterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spreadSheetID, sheetID, sheetName, characterName);
    }

    @Override
    public String toString() {
        return "WorkSheet{" +
                "sheetID=" + sheetID +
                ", sheetName='" + sheetName + '\'' +
                ", characterName='" + characterName + '\'' +
                '}';
    }
}
