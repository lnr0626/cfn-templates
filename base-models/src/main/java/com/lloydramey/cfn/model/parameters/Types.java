/*
 * Copyright 2017 Lloyd Ramey <lnr0626@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lloydramey.cfn.model.parameters;

import com.lloydramey.cfn.model.ParameterType;

public class Types {
    public static final ParameterType.Single Str = new ParameterType.Single("String");
    public static final ParameterType.Single Number = new ParameterType.Single("Number");
    public static final ParameterType.Single CommaDelimitedList = new ParameterType.Single("CommaDelimitedList");
}
