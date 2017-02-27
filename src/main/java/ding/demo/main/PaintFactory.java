package ding.demo.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import ding.demo.manager.FileManager;
import ding.demo.manager.PaintBatchManager;
import ding.demo.manager.ParseFileManager;
import ding.demo.model.PaintRequestResponse;
import ding.demo.model.TestCase;


/**
 * Root resource (exposed at "paintfactory" path)
 */
@Path("paintfactory")
public class PaintFactory {
	public static final String ROOT_URI = "/Users/ding/Tmp/";
	public static final String RESULT_SUFFIX = ".result";
	
	@Context
	UriInfo uriInfo;
	
	@Context
	Request request;

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Path("/paint-request")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public PaintRequestResponse paintFactory(
    		@FormDataParam("file") InputStream uploadedInputStream,
    		@FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("path") String path
    		) {
    	String uploadedFileFolderLocation = ROOT_URI + fileDetail.getFileName();
    	initialInputFileFolder(uploadedFileFolderLocation);
    	String uploadedFileLocation = uploadedFileFolderLocation + "/" + fileDetail.getFileName();
    	String resultFileLocation = uploadedFileFolderLocation + "/" + fileDetail.getFileName() + RESULT_SUFFIX;
    	FileManager.getInstance().writeToFile(uploadedInputStream, uploadedFileLocation);
    	processInputFile(uploadedFileLocation, resultFileLocation);
    	
        return generatePaintFactoryResponse(fileDetail.getFileName(), fileDetail.getFileName() + RESULT_SUFFIX);
    }
    
    @GET
    @Path("/paint-request/{folderName}/{fileName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getResultFile(
    		@PathParam("folderName") String folderName,
    		@PathParam("fileName") String fileName
    		) {
    	URI uri = UriBuilder.fromPath(ROOT_URI).path(folderName).path(fileName).build();
    	
        return Response.ok().entity(uri.toString()).build();
    }
    
    private boolean initialInputFileFolder(String folderUri){
    	try {
			FileManager.getInstance().deleteFile(folderUri);
			return new File(folderUri).mkdirs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    
    
    private PaintRequestResponse generatePaintFactoryResponse(String fileName, String resultFileName) {
    	PaintRequestResponse entity = new PaintRequestResponse();
    	entity.setCreated(new Date());
    	entity.setName(fileName);
        URI selfUri = uriInfo.getAbsolutePathBuilder().
                path(fileName).
                path(fileName).
                build();
    	entity.addLink("self", selfUri.toASCIIString());
    	URI resultUri = uriInfo.getAbsolutePathBuilder().
                path(fileName).
                path(resultFileName).
                build();
    	entity.addLink("result", resultUri.toASCIIString());
    	return entity;
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
