package org.kaaproject.kaa.server.transports.coap.transport;

import org.eclipse.californium.core.CoapServer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 10/22/16
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoapHandler extends CoapServer {

    private static  int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);
    private static final Logger LOG = LoggerFactory.getLogger(CoapHandler.class);

    /*
     * Application entry point.
     */


    /**
     * Add individual endpoints listening on default CoAP port on all IPv4 addresses of all network interfaces.
     */
    private void addEndpoints() {
        for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
            // only binds to IPv4 addresses and localhost
            if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
                InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
                addEndpoint(new CoapEndpoint(bindToAddress));
            }
        }
    }

    /*
     * Constructor for a new Hello-World server. Here, the resources
     * of the server are initialized.
     */
    public CoapHandler(int port) throws SocketException {

        // provide an instance of a Hello-World resource
        add(new HelloWorldResource());
        COAP_PORT=port;
        addEndpoints();
    }

    /*
     * Definition of the Hello-World Resource
     */
    class HelloWorldResource extends CoapResource {

        public HelloWorldResource() {

            // set resource identifier
            super("kaaCoAP");

            // set display name
            getAttributes().setTitle("Kaa-Coap Resource");
        }


         @Override
         public void handleGET(CoapExchange exchange)
         {
             exchange.respond("Hello World! This is a response to GET method from Kaa Coap Server!");

         }


        @Override
        public void handlePOST(CoapExchange exchange) {

            // respond to the request
            String text=exchange.getRequestText();
            int time=0;
            if (text.split("=").length>=2) time =  Integer.parseInt(text.split("=")[1]) ;
            if(time>=17 && time<=24) //means night
                 exchange.respond("Hello World! This is a response from Kaa Coap Server! Set LCD brightness to :0.3");
            else if(time>0 && time<17)
                exchange.respond("Hello World! This is a response from Kaa Coap Server! Set LCD brightness to :1.5");
            else
                exchange.respond("Hello World! This is a response from Kaa Coap Server! Set LCD brightness to :2.0");



        }
    }

}
