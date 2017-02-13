package com.lloydramey.cfn.model.parameters;

import com.lloydramey.cfn.model.ParameterType;

public class Types {
    public static final ParameterType.Single Str = new ParameterType.Single("String");
    public static final ParameterType.Single Number = new ParameterType.Single("Number");
    public static final ParameterType.Single CommaDelimitedList = new ParameterType.Single("CommaDelimitedList");
}
