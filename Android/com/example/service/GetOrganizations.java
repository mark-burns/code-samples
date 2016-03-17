package com.example.service;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * This is a synchronous call wrap it in an AsyncTask.
 *
 * Created by mburns on 10/29/2015.
 */
public class GetOrganizations {

    private static final String SORT = "name";

    /* Define the POJO structures that Gson will deserialize the server's JSON into */
    // Be aware that the field names in these classes must exactly match the names used in the JSON or ....
    //   alternatively you can use the @SerializedName annotation if you want to give them a different name.
    static class ResponseData {
        PageResponse page;
        List<ContentData> content;
    }
    static class PageResponse {
        int number;
        int pages;
        int count;
    }
    static class ContentData {
        String id;
        String name;
        String alias;
        String imageUrl;
    }
    /* END OF deserialize POJO classes */

    interface GetOrganizationsClient {
        @GET("/organizations")
        ResponseData getOrgs(@Query("page") int page, @Query("size") int size, @Query("sort") String sort);
    }

    public static List<Organization> runCommand() {

        List<Organization> results = new ArrayList<Organization>();
        int page = 0;
        int size = 20;
        int pages = 1;

        // Create REST adapter which calls the organizations end point.
        GetOrganizationsClient client = ServiceGenerator.createService(GetOrganizationsClient.class, Constants.SERVER_ENDPOINT);

        while (page < pages) {
            // Fetch and process a list of the Contacts
            ResponseData responseData = client.getOrgs(page, size, SORT);

            if (responseData != null) {
                // Add these orgs to the list
                for (ContentData org : responseData.content) {
                    results.add(new Organization(org.id, org.name, org.alias, org.imageUrl));
                }

                pages = responseData.page.pages;
                page++;
            }
        }

        return results;
    }
}
