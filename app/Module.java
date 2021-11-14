import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;
import services.RedditApi;
import services.RedditImplemention;

/**
 * The configuration of the bindings
 *
 * @author Yongshi Liang
 */
@SuppressWarnings("unused")
public class Module extends AbstractModule implements AkkaGuiceSupport {

    /**
     * Configures the {@link RedditApi} interface is binded to
     * its implementation {@link RedditImplemention}
     */
    @Override
    public void configure() {
        bind(RedditApi.class).to(RedditImplemention.class);
    }
}
