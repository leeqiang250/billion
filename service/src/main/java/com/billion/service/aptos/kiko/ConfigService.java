package com.billion.service.aptos.kiko;

import com.billion.model.enums.Chain;
import com.billion.model.enums.Language;
import com.billion.model.dto.Config;
import com.billion.model.dto.Context;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class ConfigService {

    @Resource
    ContractService contractService;

    @Resource
    LanguageService languageService;

    @Resource
    AptosService aptosService;

    public Config get(Context context) {
        return Config.builder()
                .currentContext(context)
                .currentNode(aptosService.requestNodeCache())
                .supportChain(Chain.getKV())
                .supportLanguage(Language.getKV1())
                .supportText(languageService.getAll(context))
                .supportContract(contractService.getAll(context))
                .build();
    }


}