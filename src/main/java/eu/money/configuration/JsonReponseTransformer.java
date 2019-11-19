package eu.money.configuration;

import spark.ResponseTransformer;
import eu.money.util.JsonMapper;

public class JsonReponseTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) {
        return JsonMapper.toJson(model);
    }

}
