package com.billion.model.dto;

import com.aptos.request.v1.response.ResponseNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings(value = {"rawtypes"})
public class Config implements Serializable {

    Context currentContext;

    ResponseNode currentNode;

    Map supportChain;

    Map supportLanguage;

    Map supportText;

    Map supportContract;

}