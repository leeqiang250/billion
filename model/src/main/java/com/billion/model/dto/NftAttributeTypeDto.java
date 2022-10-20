package com.billion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NftAttributeTypeDto {
    String type;
    String meta;
}
