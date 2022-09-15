package com.billion.service.aptos.kiko;

import com.billion.model.constant.Chain;
import com.billion.model.constant.Language;
import com.billion.model.dto.Config;
import com.billion.model.dto.Context;
import lombok.NonNull;
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

    public Config get(@NonNull Context context) {
        return Config.builder()
                .context(context)
                .supportChain(Chain.map())
                .supportLanguage(Language.map())
                .text(languageService.getLanguage(context))
                .contract(contractService.getContract(context))
                .build();
    }

}