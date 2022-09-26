package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.RequestInfo;
import com.billion.dao.aptos.kiko.LogChainMapper;
import com.billion.model.entity.LogChain;
import com.billion.service.aptos.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Slf4j
@Service
public class LogChainServiceImpl extends AbstractCacheService<LogChainMapper, LogChain> implements LogChainService {

    @Override
    public void add(RequestInfo info) {
        try {
            var logChain = LogChain.builder()
                    .isResult(info.isResult())
                    .path(info.getRequest().path())
                    .query(Objects.isNull(info.getRequest().query()) ? EMPTY : info.getRequest().query().toString())
                    .body(Objects.isNull(info.getRequest().body()) ? EMPTY : info.getRequest().body().toString())
                    .message(Objects.isNull(info.getMessage()) ? EMPTY : info.getMessage())
                    .errorCode(Objects.isNull(info.getErrorCode()) ? EMPTY : info.getErrorCode())
                    .vmErrorCode(Objects.isNull(info.getVmErrorCode()) ? EMPTY : info.getVmErrorCode())
                    .build();

            super.save(logChain);
        } catch (Exception e) {
            log.error("{}", e);
        }
    }

}