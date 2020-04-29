
package kaufland.com.hydraapi;

interface IHydraApi {


Map findById(String id);

List findByType(String type);

List findByTypeAndItemType(String type, String itemType);

void upsert(in Map doc);

void delete(String id);

String syncState(String id);

boolean makeReady();

void logout();

List searchByFtsIndexAndValue(String indexName, String value, int limit);

}
