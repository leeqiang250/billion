package com.billion.model.dto;

import com.billion.model.enums.Chain;
import com.billion.model.enums.Language;
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
        return Chain.of(this.chain).getCode();
    }

    public String getLanguage() {
        return Language.of(this.language).getCode();
    }

    public String key() {
        return "::" + this.getChain() + "-" + this.getLanguage();
    }

}