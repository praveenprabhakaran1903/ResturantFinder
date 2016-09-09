package com.praveen.restaurant_find;

import android.content.Context;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

 class YelpTest {

  OAuthService service;
  Token accessToken;
  final  String consumerKey = "0Al7l4FGsHY6mqIkZJPrag";
  final  String consumerSecret = "oYMk21LqA-0RQXwFJ0-wLlM-5WE";
  final  String token = "xwM1CN6dCM0LrBOckgnRciDxQvOjmFn5";
  final  String tokenSecret = "wb6O26H2dCjr3khS2q4J2mDbTtg";

  YelpTest getYelp(Context context) {
	  return new YelpTest(context.getString(R.string.consumer_key), context.getString(R.string.consumer_secret),
			  context.getString(R.string.token), context.getString(R.string.token_secret));
  }

  /**
   * Setup the Yelp API OAuth credentials.
   *
   * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
   *
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
  public YelpTest(String consumerKey, String consumerSecret, String token, String tokenSecret) {
    this.service = new ServiceBuilder().provider(YelpApi.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
    this.accessToken = new Token(token, tokenSecret);
  }
    public YelpTest() {
        this.service = new ServiceBuilder().provider(YelpApi.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

  /**
   * Search with term and location.
   *
   * @param term Search term
   * @param latitude Latitude
   * @param longitude Longitude
   * @return JSON string response
   */
  public String search(String term, double latitude, double longitude, String limit, String radius_filter) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
      request.addQuerystringParameter("limit", limit);
      request.addQuerystringParameter("radius_filter", radius_filter);

      this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

  public String search(String term, String location) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("location", location);
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

  /* CLI
  /public static void main(String[] args) {
    // Update tokens here from Yelp developers site, Manage API access.



    String response = yelp.search("burritos", 30.361471, -87.164326);
    //String response = yelp.search("taco","san jose");

    System.out.println(response);
  }*/
}
