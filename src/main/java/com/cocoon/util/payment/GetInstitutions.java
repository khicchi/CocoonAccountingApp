package com.cocoon.util.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import yapily.ApiClient;
import yapily.ApiException;
import yapily.sdk.ApiListResponseOfInstitution;
import yapily.sdk.Institution;
import yapily.sdk.InstitutionsApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@NoArgsConstructor
public class GetInstitutions {

    public static List<String> institutions;

    public static void main(String[] args) {
        System.out.println("List institutions from Yapily API!");

        try {
            // Set access credentials
            ApiClient defaultClient = ApiClientUtils.basicAuth();

            System.out.println("Configured application credentials for API: " + defaultClient.getBasePath());

            // Create InstitutionsApi API client
            final InstitutionsApi institutionsApi = new InstitutionsApi(defaultClient);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Get all institutions
            System.out.println("Get Institutions from API: /institutions");
            ApiListResponseOfInstitution institutionsResponse = institutionsApi.getInstitutionsUsingGET("");

            // Print only the names
            final List<String> institutionsNamesList = institutionsResponse.getData().stream()
                    .map(Institution::getName)
                    .peek(obj -> institutions.add(obj))
                    .collect(Collectors.toList());
            System.out.println("All institutions:");
            System.out.println(gson.toJson(institutionsNamesList));

            // Get only one institution
            String institutionId = institutionsResponse.getData().get(0).getId();

            System.out.println("Get Institution from API: /institutions/{institutionId}");
            Institution institution = institutionsApi.getInstitutionUsingGET(institutionId, "");

            System.out.println("One institution:");
            System.out.println(gson.toJson(institution));

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
