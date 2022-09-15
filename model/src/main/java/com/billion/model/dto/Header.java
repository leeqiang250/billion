package com.billion.model.dto;

import com.billion.model.constant.Chain;
import com.billion.model.constant.Language;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author liqiang
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Header implements Serializable {

    Chain chain;

    Language language;

    public Chain getChain() {
        return Objects.isNull(this.chain) ? Chain.APTOS : this.chain;
    }

    public Language getLanguage() {
        return Objects.isNull(this.language) ? Language.ZH_TC : this.language;
    }

}