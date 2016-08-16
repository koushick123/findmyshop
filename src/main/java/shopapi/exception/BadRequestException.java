package shopapi.exception;

/**
 * Created by koushick on 11-Jul-16.
 */
public class BadRequestException extends Exception{

	public static final String LAT_LONG_ERROR = "Latitude and Longitude should not be empty";
	
    public BadRequestException(){
        super();
    }

    public BadRequestException(String error_msg){
        super(error_msg);
    }
}
