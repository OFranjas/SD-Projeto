package main;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class apirest {

    public static void main(String[] args) {
        apirest apirest = new apirest();
        // String[] res = apirest.topStories();

    }

    private String[] topStories() {

        try {

            // api rest GET request to get top stories
            // https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty

            // Open a HTTP connection to the URL
            URL url = new URL("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // By default it is GET request
            con.setRequestMethod("GET");

            // Read the response from the input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }

            // Close the connection
            in.close();
            con.disconnect();

            // printing result from response
            System.out.println(response.toString());

            // Remove [ and ] from response
            response.deleteCharAt(0);
            response.deleteCharAt(response.length() - 1);

            // Split response by , and remove spaces after
            String[] parts = response.toString().split(",");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }

            // Variable to store all the urls
            String[] urls = new String[500];

            // For each story id, get the story url

            for (int i = 0; i < 500; i++) {

                // Open a HTTP connection to the URL
                url = new URL("https://hacker-news.firebaseio.com/v0/item/" + parts[i]
                        + ".json?print=pretty");
                con = (HttpURLConnection) url.openConnection();

                // By default it is GET request
                con.setRequestMethod("GET");

                // Read the response from the input stream
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }

                // Get the url from the response
                String[] parts2 = response.toString().split(",");
                String url_story = "";
                for (int j = 0; j < parts2.length; j++) {
                    if (parts2[j].contains("url")) {
                        url_story = parts2[j].substring(7, parts2[j].length() - 1);
                        break;
                    }
                }

                // If the url is empty, then continue
                if (url_story.equals(""))
                    continue;

                // Remove : from the beggining of the url
                url_story = url_story.substring(1);
                url_story = url_story.substring(1);

                // Remove the ""
                url_story = url_story.substring(1, url_story.length() - 1);

                url_story = url_story.substring(1);

                // Close the connection
                in.close();
                con.disconnect();

                // print the url
                System.out.println(url_story);

                // Add the url to the array
                urls[i] = url_story;

            }

            return urls;

        } catch (Exception e) {
            System.out.println("Exception in apirest.topStories: " + e);
            return null;
        }
    }

    public static String[] userStories(String id) {

        try {
            // api rest GET request to get user stories
            // https://hacker-news.firebaseio.com/v0/user/ + id + .json?print=pretty

            // Open a HTTP connection to the URL
            URL url = new URL("https://hacker-news.firebaseio.com/v0/user/" + id + ".json?print=pretty");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // By default it is GET request
            con.setRequestMethod("GET");

            // Read the response from the input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }

            // Close the connection
            in.close();
            con.disconnect();

            // printing result from response
            // System.out.println(response.toString());

            // Remove everything before the [ and after the ]
            response.delete(0, response.indexOf("[") + 1);
            response.delete(response.indexOf("]"), response.length());

            // Split response by , and remove spaces after
            String[] parts = response.toString().split(",");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }

            // Variable to store all the urls
            String[] urls = new String[parts.length];

            // For each story id, get the story url

            for (int i = 0; i < parts.length; i++) {

                // Open a HTTP connection to the URL
                url = new URL("https://hacker-news.firebaseio.com/v0/item/" + parts[i]
                        + ".json?print=pretty");
                con = (HttpURLConnection) url.openConnection();

                // By default it is GET request
                con.setRequestMethod("GET");

                // Read the response from the input stream
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }

                // Get the url from the response
                String[] parts2 = response.toString().split(",");
                String url_story = "";
                for (int j = 0; j < parts2.length; j++) {
                    if (parts2[j].contains("url")) {
                        url_story = parts2[j].substring(7, parts2[j].length() - 1);
                        break;
                    }
                }

                // If the url is empty, then continue
                if (url_story.equals(""))
                    continue;

                // Remove : from the beggining of the url
                url_story = url_story.substring(1);
                url_story = url_story.substring(1);

                // Remove the ""
                url_story = url_story.substring(1, url_story.length() - 1);

                url_story = url_story.substring(1);

                // Close the connection
                in.close();
                con.disconnect();

                // print the url
                // System.out.println(url_story);

                // Add the url to the array
                urls[i] = url_story;

            }

            return urls;
        } catch (Exception e) {
            System.out.println("Exception in apirest.userStories: " + e);
            return null;

        }

    }

}
