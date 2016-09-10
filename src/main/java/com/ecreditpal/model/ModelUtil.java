package com.ecreditpal.model;

import org.dmg.pmml.PMML;
import org.jpmml.model.PMMLUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by xpbu on 9/9/16.
 */


public class ModelUtil {
    public static PMML readPMML(File file) throws Exception {
        try {
            InputStream is = new FileInputStream(file);
            return PMMLUtil.unmarshal(is);
        } catch (Exception e) {
            throw e;
        }
    }

}
