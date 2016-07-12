package shopapi.shopservice;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopapi.dto.ShopAddressDTO;
import shopapi.exception.BadRequestException;

import java.util.ArrayList;


@RestController
public class ShopAPIController {

    //Declare constants that can be reused.
    private static final String MAPS_API_KEY = "AIzaSyBk-YVyQPfBkJMhMwMCTe3PJuRnFipF3Bs";
    private static final String MAPS_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?key=";
    private static final String MAPS_API_PARAMETERS = "&mode=walking&units=metric";
    private static final String ORIGINS = "&origins=";
    private static final String DESTINATIONS = "&destinations=";

    //This variable will keep track of all the shops being added using POST request by the Retail Manager.
    private ArrayList<ShopAddressDTO> shopsArray = new ArrayList<ShopAddressDTO>();


    //Method to add shop details.
    @RequestMapping(value="/shops", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addShopDetails(@RequestBody ShopAddressDTO shopAddressDTO)
    {
        GeoApiContext context = new GeoApiContext().setApiKey(ShopAPIController.MAPS_API_KEY);
        try {

            if((shopAddressDTO.getShopName() == null || shopAddressDTO.getShopName().trim().isEmpty()) ||
                    shopAddressDTO.getShopNumber() == null || shopAddressDTO.getShopNumber().trim().isEmpty() ||
                    shopAddressDTO.getPostCode() == null || shopAddressDTO.getPostCode().trim().isEmpty())
            {
                throw new BadRequestException("There is no Shop Address Details to add");
            }

            //Fetch the latitude and longitude for the shop details sent.
            GeocodingResult[] results = GeocodingApi.geocode(context,shopAddressDTO.getShopNumber()+", "+shopAddressDTO.getShopName()
            +", "+shopAddressDTO.getPostCode()).await();
            if(results != null && results.length > 0)
            {
                String latitude = "" + results[0].geometry.location.lat;
                String longitude = "" + results[0].geometry.location.lng;

                shopAddressDTO.setLatitude(latitude);
                shopAddressDTO.setLongitude(longitude);

                //Update the shopsArray to store shop location.
                shopsArray.add(shopAddressDTO);

                return new ResponseEntity(HttpStatus.CREATED);
            }
            else if (results.length == 0)
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else
            {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Method to find the nearest shop details by using distance matrix API to compare the shortest distance between lat and long
    @RequestMapping(value = "/nearestShop", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public ShopAddressDTO fetchNearestShopDetails(@RequestParam(value = "latitude") String latitude, @RequestParam(value = "longitude") String longitude)
            throws BadRequestException
    {
        if((latitude == null || longitude == null) ||
                (latitude != null && latitude.trim().isEmpty()) || (longitude != null && longitude.trim().isEmpty()))
        {
            throw new BadRequestException("Latitude and Longitude should not be empty");
        }

        return shortestDistanceShop(latitude,longitude);
    }

    private ShopAddressDTO shortestDistanceShop(String originLatitude,String originLongitude)
    {
        ShopAddressDTO closestShopAddress = new ShopAddressDTO();
        try {
            long shortestPath = 0;
            int shortestPathIndex = 0;
            StringBuffer distanceMatrixUrl = new StringBuffer(ShopAPIController.MAPS_API_URL);
            distanceMatrixUrl.append(ShopAPIController.MAPS_API_KEY);
            distanceMatrixUrl.append(ShopAPIController.MAPS_API_PARAMETERS);
            distanceMatrixUrl.append(ORIGINS+originLatitude+","+originLongitude);

            OkHttpClient client = new OkHttpClient();
            for(int i=0;i<shopsArray.size();i++)
            {
                StringBuffer distanceMatrixUrlUpdate = new StringBuffer(distanceMatrixUrl);
                String destinations = DESTINATIONS+shopsArray.get(i).getLatitude()+","+shopsArray.get(i).getLongitude();
                distanceMatrixUrlUpdate.append(destinations);

                Request request = new Request.Builder()
                        .url(distanceMatrixUrlUpdate.toString())
                        .build();
                Response response = client.newCall(request).execute();

                //Parse the complete response to extract the distance value in meters and compare for smallest value.
                String distanceResp = response.body().string();
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(distanceResp);
                JSONArray rows = (JSONArray) jsonObject.get("rows");
                for (int r=0;r<rows.size();r++)
                {
                    JSONObject elements_arr = (JSONObject) rows.get(r);
                    JSONArray elements = (JSONArray) elements_arr.get("elements");
                    for(int e=0;e<elements.size();e++)
                    {
                        JSONObject elem = (JSONObject)elements.get(e);
                        JSONObject distance = (JSONObject) elem.get("distance");
                        long dist = new Long(distance.get("value")+"").longValue();
                        if(shortestPath == 0)
                        {
                            shortestPath = dist;
                            shortestPathIndex = i;
                        }
                        else if(dist < shortestPath)
                        {
                            shortestPath = dist;
                            shortestPathIndex = i;
                        }
                    }
                }
            }
            closestShopAddress.setShopNumber(shopsArray.get(shortestPathIndex).getShopNumber());
            closestShopAddress.setShopName(shopsArray.get(shortestPathIndex).getShopName());
            closestShopAddress.setPostCode(shopsArray.get(shortestPathIndex).getPostCode());
            closestShopAddress.setLatitude(shopsArray.get(shortestPathIndex).getLatitude());
            closestShopAddress.setLongitude(shopsArray.get(shortestPathIndex).getLongitude());
            closestShopAddress.setClosestDistance(shortestPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return closestShopAddress;
    }
}
