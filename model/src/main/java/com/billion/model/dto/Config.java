package com.billion.model.dto;

import com.billion.model.constant.Chain;
import com.billion.model.constant.Language;
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
public class Config implements Serializable {

    Chain[] chain;

    Map language;

    Map text;

    Map contract;

}