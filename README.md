# modelFramework
Simulate the model execution on Live. <br />
Flow:<br />
1. get JSON/CSV raw variables <br />
2. Variable Engine: readin raw variable and derive new variables. Put all the variables in variablePool <br />
3. Model score Engine: execute the model and return the prediction result <br />

Score Engine: <br />
1. accepts PMML version 3.0, 3.1, 3.2, 4.0, 4.1, 4.2 and 4.3. <br />
2. accepts binning, woe and zscale as long as defined in PMML file <br />
3. pass the test for linear regression, neural network, tree, kmean, svm... <br />

 
 