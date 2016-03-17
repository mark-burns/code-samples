package com.example.helper;

import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by markburns on 3/1/15.
 */
public class AssetFileLoader {

    public static List<Contact> loadAsset(AssetManager assets) {

        List<Contact> contacts = new ArrayList<Contact>();

        try (InputStream is = assets.open("contacts.json")) {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String jsonStr = new String(buffer, "UTF-8");

            JSONObject obj = new JSONObject(jsonStr);
            JSONArray arry = obj.getJSONArray("contacts");

            for (int i = 0; i < arry.length(); i++) {
                JSONObject item = arry.getJSONObject(i);
                Contact contact = new Contact();
                contact.setFirstName(item.getString("firstName"));
                contact.setLastName(item.getString("lastName"));
                contact.setAge(item.getString("age"));
                contact.setStreetAddress(item.getString("streetAddress"));
                contact.setCity(item.getString("city"));
                contact.setState(item.getString("state"));
                contact.setPostalCode(item.getString("postalCode"));
                contacts.add(contact);
            }

        } catch (Exception ex) {

        }

        return contacts;
    }
}
