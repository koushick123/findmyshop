package shopapi.dto;

/**
 * Created by koushick on 10-Jul-16.
 */
public class ShopAddressDTO {

    private String shopName;
    private String shopNumber;
    private String postCode;
    private String latitude;
    private String longitude;

    public long getClosestDistance() {
        return closestDistance;
    }

    public void setClosestDistance(long closestDistance) {
        this.closestDistance = closestDistance;
    }

    private long closestDistance;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
