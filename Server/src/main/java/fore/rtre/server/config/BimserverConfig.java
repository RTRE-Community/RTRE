package fore.rtre.server.config;

import fore.rtre.server.Main;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SExtendedDataSchema;
import org.bimserver.interfaces.objects.SPluginInformation;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.ServiceException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class BimserverConfig {

    static public JsonBimServerClientFactory factory;
    static public BimServerClient client;


    @PostConstruct
    public void BimserverInit(){

            try {
                factory = new JsonBimServerClientFactory(Main.BimPort);
                client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));

            } catch (BimServerClientException | ServiceException | ChannelConnectionException e) {
                System.out.println("Initializing Bimserver...");
                try{
                    factory = new JsonBimServerClientFactory(Main.BimPort);
                    factory.create().getAdminInterface().setup("http://localhost:8082", "", "", "/img/bimserver.png", "Administrator", "admin@admin.com", "password");
                    client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));
                    client.getServiceInterface().checkInternetConnection();
                    List<SExtendedDataSchema> allSchemas =
                            client.getServiceInterface().getAllRepositoryExtendedDataSchemas(true);

                    for (int i = 0; i < allSchemas.size(); i++) {
                        if (allSchemas.get(i).getName().startsWith("GEOMETRY")){
                            continue;
                        }
                        client.getServiceInterface().addExtendedDataSchema(allSchemas.get(i));
                    }
                    client.getSettingsInterface().setPluginStrictVersionChecking(true);
                    client.getPluginInterface().hasPreBuiltPlugins();
                    client.getPluginInterface().getInstalledPluginBundles();

                    //  client.getPluginInterface().getPluginBundle("https://repo1.maven.org/maven2","org.opensourcebim","bimviews");
                    List<SPluginInformation> pluginInformation = client.getPluginInterface().getPluginInformation("central (https://repo1.maven.org/maven2/, default, releases snapshots)","org.opensourcebim","bimviews","0.0.184");
                    client.getPluginInterface().installPluginBundle("central (https://repo1.maven.org/maven2/, default, releases snapshots)","org.opensourcebim","bimviews","0.0.184", pluginInformation);

                    //  client.getPluginInterface().getPluginBundle("https://repo1.maven.org/maven2", "org.opensourcebim", "console");
                    pluginInformation = client.getPluginInterface().getPluginInformation("central (https://repo1.maven.org/maven2/, default, releases snapshots)","org.opensourcebim","console", "0.0.67");
                    client.getPluginInterface().installPluginBundle("central (https://repo1.maven.org/maven2/, default, releases snapshots)","org.opensourcebim","console", "0.0.67", pluginInformation);


                    java.util.List<org.bimserver.interfaces.objects.SPluginBundle> allAvailablePlugins = client.getPluginInterface().getAvailablePluginBundles();
                    //  SPluginBundle sPluginBundle = client.getPluginInterface().getPluginBundle("https://repo1.maven.org/maven2", "org.opensourcebim", "ifcplugins");
                    pluginInformation = client.getPluginInterface().getPluginInformation("central (https://repo1.maven.org/maven2/, default, releases+snapshots)", "org.opensourcebim", "ifcplugins", "0.0.99");
                    for (int i = 0; i < pluginInformation.size(); i++) {
                        pluginInformation.get(i).setInstallForAllUsers(true);
                        pluginInformation.get(i).setInstallForNewUsers(true);
                        pluginInformation.get(i).setEnabled(true);
                    }
                    client.getPluginInterface().installPluginBundle("central (https://repo1.maven.org/maven2/, default, releases+snapshots)", "org.opensourcebim", "ifcplugins", "0.0.99", pluginInformation);
                    client.getPluginInterface().setDefaultRenderEngine(client.getPluginInterface().getAllRenderEngines(false).get(0).getOid());
                    System.out.println("Setup done and connected to Bimserver!");

                } catch (ServerException serverException) {
                    serverException.printStackTrace();
                } catch (UserException userException) {
                    userException.printStackTrace();
                } catch (BimServerClientException bimServerClientException) {
                    bimServerClientException.printStackTrace();
                } catch (ServiceException serviceException) {
                    serviceException.printStackTrace();
                } catch (ChannelConnectionException channelConnectionException) {
                    channelConnectionException.printStackTrace();
                }
            }

    }
}
