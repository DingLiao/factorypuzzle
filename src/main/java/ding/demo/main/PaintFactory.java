package ding.demo.main;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import ding.demo.manager.FileManager;
import ding.demo.manager.PaintBatchManager;
import ding.demo.manager.ParseFileManager;
import ding.demo.model.TestCase;


/**
 * Root resource (exposed at "paintfactory" path)
 */
@Path("paintfactory")
public class PaintFactory {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response paintFactory(
    		@FormDataParam("file") InputStream uploadedInputStream,
    		@FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("path") String path
    		) {
    	String uploadedFileLocation = "/Users/ding/Tmp/" + fileDetail.getFileName();
    	String resultFileLocation = "/Users/ding/Tmp/" + fileDetail.getFileName() + ".result";
    	FileManager.getInstance().writeToFile(uploadedInputStream, uploadedFileLocation);
    	processInputFile(uploadedFileLocation, resultFileLocation);
    	
        return generatePaintFactoryResponse(resultFileLocation);
    }
    
    @GET
    @Path("/file/{fileName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getResultFile(
    		@PathParam("fileName") String fileName
    		) {
    	String resultFileLocation = "/Users/ding/Tmp/" + fileName;
    	
        return generatePaintFactoryResponse(resultFileLocation);
    }
    
    private Response generatePaintFactoryResponse(String resultFileLocation) {
    	return Response.status(200).entity(resultFileLocation).build();
    }
    
    private void processInputFile(final String uploadedFileLocation, final String resultFileLocation) {
    	new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					List<TestCase> testCaseList;
					testCaseList = ParseFileManager.getInstance().parseInputFile(uploadedFileLocation);// get Objects from input file
			    	PaintBatchManager manager = PaintBatchManager.getInstance();
					for(TestCase tc: testCaseList) {
						manager.analyzeTestCase(tc);
					}
					FileManager.getInstance().generateResultFile(resultFileLocation, manager.getSolutionsForAllTestCase());// write results into output file
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    		
    	}).start();;
    }
    
}
