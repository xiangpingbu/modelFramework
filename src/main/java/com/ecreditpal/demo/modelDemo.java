package com.ecreditpal.com.ecreditpal.demo;

import java.io.*;
import java.util.*;


import com.ecreditpal.model.ModelUtil;
import com.ecreditpal.model.ScoreEngine;
import com.ecreditpal.variables.VariableEngine;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.jpmml.evaluator.ProbabilityDistribution;
import org.dmg.pmml.FieldName;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Created by xpbu on 9/9/16.
 */
public class modelDemo {

    public static void main(String... args) throws Exception{
        //csvTest();
        JsonTest();
    }

    //for demo purpose - direct csv

    public static void csvTest() throws Exception{

        String testData = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/Iris.csv";
        String testPmml = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/logreg_iris.pmml";
        String scoreResult = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/score_lr_iris.csv";


        /*
        String testData = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/Audit.csv";
        String testPmml = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/logreg.pmml";
        String scoreResult = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/score_lr_audit.csv";
        */


        File testPmmlFile = new File(testPmml);

        FileInputStream fis = new FileInputStream(testData);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));


        VariableEngine ve = new VariableEngine();
        ScoreEngine se = new ScoreEngine(ModelUtil.readPMML(testPmmlFile));


        String line = null;
        ArrayList<HashMap<FieldName, String>> records = new ArrayList<HashMap<FieldName, String>>();
        String header = br.readLine();
        String sep = ",";
        while ((line = br.readLine()) != null) {
            ve.variableDeriveCSV(header, line, sep);

        }

        PrintWriter output = new PrintWriter(scoreResult);
        List<Map<FieldName,?>> result = se.execute();

        for(Map m: result){

            Iterator it = m.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ProbabilityDistribution a = (ProbabilityDistribution)pair.getValue();
                output.write(a.getResult().toString()+"\n");
            }
        }
        output.close();

    }


    // for demo purpose - dummy Json
    public static void JsonTest() throws Exception{
        String testData = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/Iris.json";
        String testPmml = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/logreg_iris.pmml";
        String scoreResult = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/score_lr_iris_json.csv";

        File testPmmlFile = new File(testPmml);

        VariableEngine ve = new VariableEngine();
        ScoreEngine se = new ScoreEngine(ModelUtil.readPMML(testPmmlFile));

        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader(testData));
            JSONArray jsonArray =  (JSONArray) obj;
            for (int i=0; i<jsonArray.size(); i++){
                ve.variableDeriveJson((JSONObject) jsonArray.get(i));

            }

            PrintWriter output = new PrintWriter(scoreResult);
            List<Map<FieldName,?>> result = se.execute();

            for(Map m: result){

                Iterator it = m.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    ProbabilityDistribution a = (ProbabilityDistribution)pair.getValue();
                    output.write(a.getResult().toString()+"\n");
                }
            }
            output.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }


    //for demo purpose - dummy Json
    public static void dummyJsonPrepare() throws Exception{
        String IrisCsv = "/Users/xpbu/IdeaProjects/modelframework/src/main/resources/Iris.csv";
        String IrisJson ="/Users/xpbu/IdeaProjects/modelframework/src/main/resources/Iris.json";
        dummyJsonGenerate(IrisCsv,IrisJson);
    }

    //for demo purpose - dummy Json
    public static void dummyJsonGenerate(String csvFilePath, String jsonPath) throws Exception{
        File input = new File(csvFilePath);
        File output = new File(jsonPath);
        List<Map<?,?>> data = readObjectsFromCsv(input);
        writeAsJson(data,output);
    }

    //for demo purpose - dummy Json
    public static List<Map<?, ?>> readObjectsFromCsv(File file) throws IOException {
        CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader(Map.class).with(bootstrap).readValues(file);

        return mappingIterator.readAll();
    }

    //for demo purpose - dummy Json
    public static void writeAsJson(List<Map<?, ?>> data, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, data);
    }
}
