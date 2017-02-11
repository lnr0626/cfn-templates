package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class ResourceProperties(@JsonIgnore val type: String)
