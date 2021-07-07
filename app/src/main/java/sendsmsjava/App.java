/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package sendsmsjava;



import com.fasterxml.jackson.databind.ObjectMapper;
// Find your serviceplan_id and Token at https://dashboard.sinch.com/sms/api
// Install the Java helper library at https://developers.sinch.com/docs/sms/sdks/java/
// Find your sinch number at // https://dashboard.sinch.com/numbers/your-numbers/numbers
import com.sinch.xms.ApiConnection;
import com.sinch.xms.SinchSMSApi;
import com.sinch.xms.api.MtBatchTextSmsCreate;
import com.sinch.xms.api.MtBatchTextSmsResult;
import com.sinch.xms.api.MoTextSms;
import static spark.Spark.*;
;
public class App {
	private static final String SERVICE_PLAN_ID = "60c1b2b7ec234896a811fd5ec3c66d1c";
	private static final String TOKEN = "e57d88d6d9164b749ce32d00264d97c5";
	private static ApiConnection conn;

	public static void main(String[] args) {
        ApiConnection conn = ApiConnection
								.builder()
								.servicePlanId(SERVICE_PLAN_ID)
								.token(TOKEN)
								.start();
        get("/", (req, res) -> "The server is up and running");
      
        post("/", (request, response) -> {
        MoTextSms message = new ObjectMapper().readValue(request.body(), MoTextSms.class);
        String[] RECIPIENTS = { message.sender() };
        String SENDER = message.recipient();
        MtBatchTextSmsCreate reply = SinchSMSApi
										.batchTextSms()
										.sender(SENDER)
										.addRecipient(RECIPIENTS)
				.body("You sent:" + message.body()).build();
        	try {
			// if there is something wrong with the batch
			// it will be exposed in APIError
			MtBatchTextSmsResult batch = conn.createBatch(reply);
			System.out.println(batch.id());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("incoming message:" + message);
        return "incoming message:" + message;
    // Create something
});
	}
}