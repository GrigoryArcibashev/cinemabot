import org.picocontainer.DefaultPicoContainer;
import userParametersRepository.dataBase.DataBase;
import userParametersRepository.dataBase.RedisDataBase;

public class Dependencies {
    public DefaultPicoContainer dependencies;

    public Dependencies(){
        this.dependencies  = new DefaultPicoContainer();
        dependencies.addComponent(DataBase.class, new RedisDataBase());
    }

}
