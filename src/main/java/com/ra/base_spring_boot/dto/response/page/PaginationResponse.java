package com.ra.base_spring_boot.dto.response.page;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationResponse<T> {
    private List<T> items;
    private PaginationDTO pagination;
}
