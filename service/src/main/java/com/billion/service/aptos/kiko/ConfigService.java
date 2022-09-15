package com.billion.service.aptos.kiko;

import com.billion.model.constant.Chain;
import com.billion.model.constant.Language;
import com.billion.model.dto.Config;
import com.billion.model.dto.Header;
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

    public Config get(@NonNull Header header) {
        return Config.builder()
                .chain(Chain.values())
                .language(Language.map())
                .text(languageService.getLanguage(header.getLanguage().getCode().getCode()))
                .contract(contractService.getContract(header.getChain().getCode()))
                .build();
    }

}