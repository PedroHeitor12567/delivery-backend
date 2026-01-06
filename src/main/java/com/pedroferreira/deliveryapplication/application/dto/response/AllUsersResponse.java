package com.pedroferreira.deliveryapplication.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllUsersResponse {
    private List<CustomerResponse> customers;
    private List<SellerResponse> sellers;
    private Integer totalCustomers;
    private Integer totalSellers;
    private Integer totalActiveUsers;
}
