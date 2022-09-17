package com.billion.model.dto;

import com.billion.model.constant.Chain;
import com.billion.model.constant.Language;
import lombok.*;

import java.io.Serializable;

/**
 * @author liqiang
 */
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Context implements Serializable {

    @Getter
    @Setter
    String chain;

    @Getter
    @Setter
    String language;

    public String getChain() {
        return Chain.of(this.chain).code();
    }

    public String getLanguage() {
        return Language.of(this.language).toString();
    }

}