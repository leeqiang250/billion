package com.billion.service.aptos.kiko;

import com.billion.model.constant.Chain;
import com.billion.model.constant.Language;
import com.billion.model.dto.Config;
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

    public Config get() {

        return Config.builder()
                .chain(Chain.values())
                .language(Language.map())
                .text(languageService.getLanguage(Language.CHT.getCode()))
                .contract(contractService.getContract(Chain.APTOS.getCode()))
                .build();
    }

}