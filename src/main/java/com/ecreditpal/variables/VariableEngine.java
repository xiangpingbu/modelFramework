package com.ecreditpal.variables;

import org.dmg.pmml.FieldName;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by xpbu on 9/9/16.
 */
public class VariableEngine {

    public static List<HashMap<FieldName, String>> variablePool = new ArrayList<HashMap<FieldName, String>>();


    public Map<FieldName, String>  convertFromJson(JSONObject input){

        Map<FieldName, String> records = new HashMap<FieldName, String>();
        for(Object key: input.keySet()) {
            records.put(FieldName.create(key.toString()), input.get(key).toString());
        }
        return records;
    }

    public HashMap<FieldName, String>  convertFromCSV(String header, String line, String sep){

        HashMap<FieldName, String> records = new HashMap<FieldName, String>();
        String[] varNames = header.split(sep);
        String[] varValues = line.split(sep);
        if(varNames.length != varValues.length){
            throw new IllegalArgumentException("header length: "+varNames.length+" data length: "+varValues.length);
        }
        for(int i=0; i<varNames.length; i++){
            records.put(FieldName.create(varNames[i]),varValues[i]);
        }

        return records;
    }

    //each type of variable should have its own derive function

    public void variableDeriveJson(JSONObject input){

        Map<FieldName,String> rawVarList = convertFromJson(input);
        variableDerive(rawVarList);
    }

    public void variableDeriveCSV(String header, String line, String sep){

        Map<FieldName,String> rawVarList = convertFromCSV(header,line, sep);
        variableDerive(rawVarList);
    }


    public void variableDerive(Map<FieldName,String> rawVarList){

        //put all raw vars to the pool
        Iterator it = rawVarList.entrySet().iterator();

        HashMap<FieldName,String> varList = new HashMap<FieldName, String>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            varList.put((FieldName) pair.getKey(),(String)pair.getValue());
        }

        //derive logic is here
        //put back to variable pool


        //after all done
        variablePool.add(varList);
    }
}
