package com.cmit.kapok.system.controller.transaction;

import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class Transaction {
    @Autowired
    private ObjectMapper objectMapper;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list() throws JsonProcessingException {
        Map reMap = new HashMap();
        String data = "{\"total\":20,\"items\":[{\"order_no\":\"C8c9Bdc2-D096-88cC-2cD1-bB11954e3B8B\",\"timestamp\":1418517089812,\"username\":\"Margaret Jones\",\"price\":12024.62,\"status\":\"pending\"},{\"order_no\":\"ddcDBEcE-DB4e-2116-A4C9-6dcFbfFBaEAe\",\"timestamp\":1418517089812,\"username\":\"Anthony Anderson\",\"price\":1728,\"status\":\"pending\"},{\"order_no\":\"ccF4e3FC-eD36-36bA-9E12-4eDf4cEf8Cc2\",\"timestamp\":1418517089812,\"username\":\"Richard Clark\",\"price\":2517,\"status\":\"pending\"},{\"order_no\":\"c1A8b0BA-be3b-7da5-5Bf3-95C2Ab6546C2\",\"timestamp\":1418517089812,\"username\":\"George Jones\",\"price\":3816.4,\"status\":\"pending\"},{\"order_no\":\"AeBD6043-bF64-Aad7-BEe1-BCBdA5422624\",\"timestamp\":1418517089812,\"username\":\"Anthony Robinson\",\"price\":12687,\"status\":\"success\"},{\"order_no\":\"Bcc1E1Ce-B4DE-8fFe-F31B-265c69D4Ce9E\",\"timestamp\":1418517089812,\"username\":\"Laura Davis\",\"price\":10204,\"status\":\"pending\"},{\"order_no\":\"7c6144dc-eAB8-7a0A-AFEb-2fd84be44Ad6\",\"timestamp\":1418517089812,\"username\":\"Gary Davis\",\"price\":6088.3,\"status\":\"success\"},{\"order_no\":\"326cEfF2-6298-e8Ad-F36D-a6B22c6bbF10\",\"timestamp\":1418517089812,\"username\":\"Anthony Brown\",\"price\":7112.8,\"status\":\"pending\"},{\"order_no\":\"156647B3-e80C-ffEe-63b9-994Bdd743aAE\",\"timestamp\":1418517089812,\"username\":\"Angela Perez\",\"price\":5922.97,\"status\":\"success\"},{\"order_no\":\"D2BC32b3-A5CB-fC92-60c9-D4CbEfbC43e2\",\"timestamp\":1418517089812,\"username\":\"David Walker\",\"price\":5306.94,\"status\":\"pending\"},{\"order_no\":\"915cdCAB-5ADD-A7D5-C6CC-06B3d64C06eD\",\"timestamp\":1418517089812,\"username\":\"James Lopez\",\"price\":2420.74,\"status\":\"pending\"},{\"order_no\":\"2e4AED51-18DD-CeE6-7A94-23aaf9BF95C3\",\"timestamp\":1418517089812,\"username\":\"Daniel Gonzalez\",\"price\":12651.4,\"status\":\"success\"},{\"order_no\":\"DBb445F5-d5CF-bBed-0E5b-71f1F1Dc7Fd4\",\"timestamp\":1418517089812,\"username\":\"Eric Williams\",\"price\":13814.8,\"status\":\"success\"},{\"order_no\":\"863DBBfE-8abE-f4Af-b88c-0CbcC9CD1dCE\",\"timestamp\":1418517089812,\"username\":\"Dorothy Rodriguez\",\"price\":13501.7,\"status\":\"pending\"},{\"order_no\":\"aCbed674-6bc9-39e6-61F8-9baef5f0Ac8e\",\"timestamp\":1418517089812,\"username\":\"Helen Taylor\",\"price\":11269.46,\"status\":\"pending\"},{\"order_no\":\"3DBEf9d1-cf0E-0b09-9E90-defc7bAbdA95\",\"timestamp\":1418517089812,\"username\":\"Michelle Clark\",\"price\":13499.9,\"status\":\"pending\"},{\"order_no\":\"7Fda1ACB-a3Fe-8Ead-2beb-Cbeae5E657df\",\"timestamp\":1418517089812,\"username\":\"Cynthia Davis\",\"price\":2399.3,\"status\":\"success\"},{\"order_no\":\"1D6BfBcc-59cC-DFE8-72D0-8dbbb2f22d7B\",\"timestamp\":1418517089812,\"username\":\"Frank Davis\",\"price\":14656.49,\"status\":\"success\"},{\"order_no\":\"C3F6BcF6-edf5-Cd56-BCDd-6f9Af2Fd68D5\",\"timestamp\":1418517089812,\"username\":\"Christopher Rodriguez\",\"price\":11643,\"status\":\"success\"},{\"order_no\":\"873AD0F5-cE8e-Bef3-7eF8-F9384E3A8bc7\",\"timestamp\":1418517089812,\"username\":\"Elizabeth Williams\",\"price\":10600.7,\"status\":\"pending\"}]}";
        reMap = objectMapper.readValue(data,Map.class);
        return ResultGenerator.genSuccessResult(reMap);
    }
}
