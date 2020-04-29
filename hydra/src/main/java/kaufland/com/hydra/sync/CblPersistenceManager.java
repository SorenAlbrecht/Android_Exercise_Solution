package kaufland.com.hydra.sync;

import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import com.couchbase.lite.*;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EBean(scope = EBean.Scope.Singleton)
public class CblPersistenceManager {

    private static final String TAG = CblPersistenceManager.class.getName();

    public static final String DB_NAME = "hydra_db";

    @RootContext
    protected Context mContext;

    private Database mCachedConnection;

    public void upsert(MutableDocument doc) throws CouchbaseLiteException {
        getConnection().save(doc);
    }

    public interface ReplicatorReplicationChanged {
        void replicationChanged(ReplicatorChange change, Replicator replication);
    }


    public ResultSet executeQuery(KQueryCreator creator) throws CouchbaseLiteException {
        Query query = creator.createQuery(getConnection());
        return query.execute();
    }

    public void removeDocumentsByType(String type) throws CouchbaseLiteException {

        ResultSet results = executeQuery(database -> QueryBuilder
                .select(SelectResult.expression(Meta.id))
                .from(DataSource.database(database))
                .where(Expression.property("type").equalTo(Expression.string(type))));
        if (results != null) {
            for (Result result : results) {
                removeDocumentById(result.getString(0));
            }
        }
    }

    public void removeDocumentById(String id) throws CouchbaseLiteException {
        getConnection().delete(getConnection().getDocument(id));
    }

    @Nullable
    public Map<String, Object> findDocumentById(String id) {
        Document document = getConnection().getDocument(id);
        if (document == null) {
            return null;
        }
        Map<String, Object> result = document.toMap();
        return result;
    }

    @org.jetbrains.annotations.Nullable
    public List<Map<String, Object>> fullTextSearchForIndexAndType(String index, String value, Integer limit) throws CouchbaseLiteException {
        KQueryCreator creator = database -> {
            From builder = QueryBuilder
                    .select(SelectResult.expression(Meta.id), SelectResult.all())
                    .from(DataSource.database(database));

            FullTextExpression ftsExpression = FullTextExpression.index(index);

            return builder.where(ftsExpression.match(value)).limit(Expression.intValue(limit));
        };
        ResultSet results = executeQuery(creator);

        List<Map<String, Object>> parsed = new ArrayList<>();
        if (results != null) {
            for (Result result : results) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", result.getString(0));
                item.putAll(result.getDictionary(1).toMap());
                parsed.add(item);
            }
        }
        return parsed;
    }


    public List<Map<String, Object>> findDocumentsByType(String type) throws CouchbaseLiteException {
        return findDocumentsByExpression("type", type, null, null);
    }

    public Long getDocumentCount() {
        return getConnection().getCount();
    }

    @NotNull
    private List<Map<String, Object>> findDocumentsByExpression(String expression, String value, String expression2, String value2) throws CouchbaseLiteException {
        KQueryCreator creator = database -> {
            From builder = QueryBuilder
                    .select(SelectResult.expression(Meta.id), SelectResult.all())
                    .from(DataSource.database(database));

            //FIXME Geil machen
            if (expression2 != null && value2 != null) {
                return builder.where(Expression.property(expression).equalTo(Expression.string(value)).and(Expression.property(expression2).equalTo(Expression.string(value2))));
            } else {
                return builder.where(Expression.property(expression).equalTo(Expression.string(value)));
            }

        };
        ResultSet results = executeQuery(creator);

        List<Map<String, Object>> parsed = new ArrayList<>();
        if (results != null) {
            for (Result result : results) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", result.getString(0));
                item.putAll(result.getDictionary(1).toMap());
                parsed.add(item);
            }
        }
        return parsed;
    }

    @NotNull
    public List<?> findDocumentsByTypeAndItemType(@org.jetbrains.annotations.Nullable String type, @org.jetbrains.annotations.Nullable String itemType) throws CouchbaseLiteException {
        return findDocumentsByExpression("type", type, "item_type", itemType);
    }

    public String getPath() {
        return getConnection().getPath();
    }

    private Database getConnection() {


        if (mCachedConnection == null) {
            DatabaseConfiguration config = new DatabaseConfiguration(mContext);
            try {
                mCachedConnection = new Database(DB_NAME, config);
                mCachedConnection.createIndex("TypeItemTypeIndex",
                        IndexBuilder.valueIndex(ValueIndexItem.property("type"), ValueIndexItem.property("item_type")));
                mCachedConnection.createIndex("GtinFtsIndex",
                        IndexBuilder.fullTextIndex(FullTextIndexItem.property("all_gtins")).setLanguage(null));
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "failed to create database connection", e);
            }
        }

        return mCachedConnection;
    }

    public void invalidateConnection() {

        if (mCachedConnection != null) {
            try {
                mCachedConnection.close();
                mCachedConnection = null;
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "failed to close database connection", e);
            }
        }
    }

    public void copyDatabase(File source) throws CouchbaseLiteException {
        removeDatabase();
        Database.copy(source, DB_NAME, new DatabaseConfiguration(mContext));
    }

    public void removeDatabase() throws CouchbaseLiteException {


        //TODO we need to stop the replicator first
        Database database = getConnection();
        if (database != null) {
            database.delete();
        }
        mCachedConnection = null;
    }
}

