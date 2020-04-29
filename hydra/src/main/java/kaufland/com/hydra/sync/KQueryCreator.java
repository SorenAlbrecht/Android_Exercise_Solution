package kaufland.com.hydra.sync;

import com.couchbase.lite.Database;
import com.couchbase.lite.Query;

public interface KQueryCreator {

    Query createQuery(Database database);

}
