package com.infinity.julien.dbOps.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CollectionInfo {
    private String name;
    private long size;
    private long count;
}
