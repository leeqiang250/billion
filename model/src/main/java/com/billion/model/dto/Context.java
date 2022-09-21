package com.billion.model.dto;

import com.billion.model.enums.Chain;
import com.billion.model.enums.Language;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author liqiang
 */
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

    @Override
    public String toString() {
        return "Context[chain:" + this.getChain() + ", language:" + this.getLanguage() + "]";
    }

    public String key() {
        return this.getChain() + "-:" + this.getLanguage();
    }

    public static Set<String> set;

    static {
        set = new HashSet(Chain.values().length * Language.values().length);
        Stream.of(Chain.values()).forEach(chain -> Stream.of(Language.values()).forEach(language -> set.add(chain.getCode() + "-:" + language.getCode())));
    }

}