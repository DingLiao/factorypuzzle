package ding.demo.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 *
 */
public class Application {
    // Base URI the Grizzly HTTP server will listen on
    public static String BASE_URI;
    public static String TMP_FILE_URI;
    public static String RESULT_SUFFIX;

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in ding.demo package
        final ResourceConfig rc = new ResourceConfig().packages("ding.demo.main").register(MultiPartFeature.class);
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        initializeParameter();
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
    
    private static void initializeParameter() throws FileNotFoundException, IOException{
        System.out.println("******** initial parameter from properties *********");
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        TMP_FILE_URI = properties.getProperty("TMP_FILE_URI");
        RESULT_SUFFIX = properties.getProperty("RESULT_SUFFIX");
        BASE_URI = properties.getProperty("BASE_URI"); 
    }
}

