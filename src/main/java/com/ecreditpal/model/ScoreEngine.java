package com.ecreditpal.model;

import com.ecreditpal.variables.VariableEngine;
import org.jpmml.evaluator.*;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;

import java.util.*;

/**
 * Created by xpbu on 9/9/16.
 */

public class ScoreEngine {

    private PMML pmml;
    public ScoreEngine(PMML pmml) {
        this.pmml = pmml;
    }

    List<? extends Map<FieldName, ?>> inputRecords = new ArrayList<Map<FieldName, ?>>();

    public List<Map<FieldName, ?>> execute(List<? extends Map<FieldName, ?>> input) throws Exception {

        inputRecords.clear();
        inputRecords = input;

        ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();

        Evaluator evaluator = modelEvaluatorFactory.newModelEvaluator(pmml);

        // Perform self-testing
        evaluator.verify();

        List<InputField> inputFields = evaluator.getInputFields();
        List<InputField> groupFields = Collections.emptyList();

        if (evaluator instanceof HasGroupFields) {
            HasGroupFields hasGroupfields = (HasGroupFields) evaluator;
            groupFields = hasGroupfields.getGroupFields();
        }

        if (evaluator instanceof HasGroupFields) {
            HasGroupFields hasGroupFields = (HasGroupFields) evaluator;
            inputRecords = EvaluatorUtil.groupRows(hasGroupFields, inputRecords);
        }

        List<Map<FieldName, ?>> scoreRecords = new ArrayList<Map<FieldName, ?>>(inputRecords.size());


        Map<FieldName, FieldValue> arguments = new LinkedHashMap<FieldName, FieldValue>();

        for (Map<FieldName, ?> inputRecord : inputRecords) {
            arguments.clear();
            //prepare input variables
            for (InputField inputField : inputFields) {
                FieldName name = inputField.getName();
                FieldValue value = EvaluatorUtil.prepare(inputField, inputRecord.get(name));
                arguments.put(name, value);
            }


            //calculate score
            Map<FieldName, ?> result = evaluator.evaluate(arguments);
            scoreRecords.add(result);


        }
        return scoreRecords;
    }

    public List<Map<FieldName, ?>> execute() throws Exception {

        inputRecords.clear();
        inputRecords = VariableEngine.variablePool;

        ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();

        Evaluator evaluator = modelEvaluatorFactory.newModelEvaluator(pmml);

        // Perform self-testing
        evaluator.verify();

        List<InputField> inputFields = evaluator.getInputFields();
        List<InputField> groupFields = Collections.emptyList();

        if (evaluator instanceof HasGroupFields) {
            HasGroupFields hasGroupfields = (HasGroupFields) evaluator;
            groupFields = hasGroupfields.getGroupFields();
        }

        if (evaluator instanceof HasGroupFields) {
            HasGroupFields hasGroupFields = (HasGroupFields) evaluator;
            inputRecords = EvaluatorUtil.groupRows(hasGroupFields, inputRecords);
        }

        List<Map<FieldName, ?>> scoreRecords = new ArrayList<Map<FieldName, ?>>(inputRecords.size());


        Map<FieldName, FieldValue> arguments = new LinkedHashMap<FieldName, FieldValue>();

        for (Map<FieldName, ?> inputRecord : inputRecords) {
            arguments.clear();
            //prepare input variables
            for (InputField inputField : inputFields) {
                FieldName name = inputField.getName();
                FieldValue value = EvaluatorUtil.prepare(inputField, inputRecord.get(name));
                arguments.put(name, value);
            }


            //calculate score
            Map<FieldName, ?> result = evaluator.evaluate(arguments);
            scoreRecords.add(result);


        }
        //batch complete, clear variable pool
        VariableEngine.variablePool.clear();
        return scoreRecords;
    }

}
