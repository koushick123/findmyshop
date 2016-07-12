package shopapi.exception;

/**
 * Created by koushick on 11-Jul-16.
 */
public class BadRequestException extends Exception{

    public BadRequestException(){
        super();
    }

    public BadRequestException(String error_msg){
        super(error_msg);
    }
}
