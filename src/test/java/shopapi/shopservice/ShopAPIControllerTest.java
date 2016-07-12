package shopapi.shopservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import shopapi.dto.ShopAddressDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class ShopAPIControllerTest {

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new shopapi.shopservice.ShopAPIController()).build();
	}

	//Run a normal test.
    @Test
	public void addShops()
	{
		try {
			String url = "http://localhost:8080/shops";
			ShopAddressDTO dto = new ShopAddressDTO();
			dto.setPostCode("123456");
			dto.setShopName("Shop1");
			dto.setShopNumber("100");
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String requestJson=ow.writeValueAsString(dto);

			mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).contentType(requestJson))
					.andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    //Test URL for empty object. It should return HTTP 204 NO_CONTENT
    @Test
    public void addEmptyShops()
    {
        try {
            String url = "http://localhost:8080/shops";
            //Only create the object. Don't pass data.
            ShopAddressDTO dto = new ShopAddressDTO();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestJson=ow.writeValueAsString(dto);

            mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).contentType(requestJson))
                    .andExpect(status().isNoContent());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Test for fetching nearest store details
    @Test
    public void getClosestShop()
    {
        try {
            mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/nearestShop?latitude=12.4353245&longitude=4.3433244")
			).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
