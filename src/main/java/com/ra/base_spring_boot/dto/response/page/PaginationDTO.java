package com.ra.base_spring_boot.dto.response.page;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PaginationDTO {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;
}
