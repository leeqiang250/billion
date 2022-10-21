package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.ConfigMapper;
import com.billion.model.dto.Context;
import com.billion.model.entity.Config;
import com.billion.model.enums.Chain;
import com.billion.model.enums.Language;
import com.billion.service.aptos.AbstractCacheService;
import com.billion.service.aptos.AptosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class ConfigServiceImpl extends AbstractCacheService<ConfigMapper, Config> implements ConfigService {

    @Resource
    ContractService contractService;

    @Resource
    LanguageService languageService;

    public com.billion.model.dto.Config get(Context context) {
        return com.billion.model.dto.Config.builder()
                .currentContext(context)
                .currentNode(AptosService.requestNodeCache())
                .supportChain(Chain.getKV2())
                .supportLanguage(Language.getKV1())
                .supportText(languageService.cacheMap(context))
                .supportContract(contractService.cacheMap(context))
                .build();
    }

}