# modelFramework
Simulate the model execution on Live. 
Flow:
1. get JSON/CSV raw variables 
2. Variable Engine: readin raw variable and derive new variables. Put all the variables in variablePool
3. Model score Engine: execute the model and return the prediction result 

Score Engine: 
1. accepts PMML version 3.0, 3.1, 3.2, 4.0, 4.1, 4.2 and 4.3. 
2. accepts binning, woe and zscale as long as defined in PMML file 
3. pass the test for linear regression, neural network, tree, kmean, svm...

 
 