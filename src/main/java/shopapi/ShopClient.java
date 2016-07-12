package shopapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import shopapi.dto.ShopAddressDTO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

@SpringBootApplication
public class ShopClient {
    
    public static void main(String[] args) {

        SpringApplication.run(ShopClient.class, args);
        ShopAddressDTO request = new ShopAddressDTO();
        RestTemplate template = new RestTemplate();
        String userLatitude="", userLongitude="";

        try {
            BufferedReader bufferedReader;
            //bufferedReader = new BufferedReader(new FileReader(".\\src\\main\\java\\shopapi\\resource\\retailcoordinates.properties"));
            bufferedReader = new BufferedReader(new FileReader(".\\retailcoordinates.properties"));
            String currentLine;

            //Read all retail shop details from a file, and store them in memory. Shop details can be added or removed without changing any code.
            while((currentLine=bufferedReader.readLine()) != null)
            {
                StringTokenizer shop = new StringTokenizer(currentLine,";");
                if(shop != null)
                {
                    request.setShopNumber(shop.nextToken());
                    request.setShopName(shop.nextToken());
                    request.setPostCode(shop.nextToken());
                    ResponseEntity<ShopAddressDTO> result = template.postForEntity("http://localhost:8080/shops", request, ShopAddressDTO.class);
                    if(result.getStatusCode().equals(HttpStatus.CREATED))
                    {
                        System.out.println("");
                        System.out.println("Successfully added Shop..."+request.getShopNumber()+", "+request.getShopName()+", "+request.getPostCode());
                        System.out.println("");
                    }
                    request = new ShopAddressDTO();
                }
            }

            //Read user's location from a file.
            bufferedReader = new BufferedReader(new FileReader(".\\clientcoordinates.properties"));
            userLatitude = bufferedReader.readLine();
            userLongitude = bufferedReader.readLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResponseEntity<ShopAddressDTO> addressDTO =
                template.getForEntity("http://localhost:8080/nearestShop?latitude="+userLatitude+"&longitude="+userLongitude, ShopAddressDTO.class);

        System.out.println("");
        System.out.println("User's latitude = "+userLatitude+", longitude = "+userLongitude);
        System.out.println("");
        System.out.println("Closest Shop is : \n"+addressDTO.getBody().getShopNumber()+",\n "+addressDTO.getBody().getShopName()+",\n "+addressDTO.getBody().getPostCode());
        System.out.println("Distance to closest shop is : "+addressDTO.getBody().getClosestDistance()+" meters");
    }
}
