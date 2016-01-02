package com.lite.blackdream.business.parameter.generatorinstance;

import com.lite.blackdream.business.domain.GeneratorInstance;
import com.lite.blackdream.framework.model.Response;

/**
 * @author LaineyC
 */
public class GeneratorInstanceDeleteResponse extends Response<GeneratorInstance> {

    public GeneratorInstanceDeleteResponse(){

    }

    public GeneratorInstanceDeleteResponse(GeneratorInstance body){
        setBody(body);
    }

}
