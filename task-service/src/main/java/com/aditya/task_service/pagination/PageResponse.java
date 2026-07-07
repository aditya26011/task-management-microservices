package com.aditya.task_service.pagination;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T>{
    private List<T> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
