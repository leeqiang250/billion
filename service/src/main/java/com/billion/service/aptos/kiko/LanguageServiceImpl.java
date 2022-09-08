package com.billion.service.aptos.kiko;

import com.billion.dao.aptos.kiko.LanguageMapper;
import com.billion.model.entity.Language;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Service
public class LanguageServiceImpl extends RedisServiceImpl<LanguageMapper, Language> implements ILanguageService {

}