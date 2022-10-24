package com.billion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NftCollection implements Serializable {

    String creator;

    String collection;

    public String getNftCollectionKey() {
        return this.creator + "@" + this.collection;
    }

    public NftCollection getNftCollectionFromKey(String value) {
        var values = value.split("@");

        return NftCollection.builder()
                .creator(values[0])
                .collection(values[1])
                .build();
    }

}