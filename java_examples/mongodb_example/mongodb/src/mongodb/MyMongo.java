package mongodb;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MyMongo {

	public static void main(String args[]) {

		MyMongo mym = new MyMongo();
		mym.runMongo();
	}

	public void runMongo() {
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DB db = mongoClient.getDB("mydb");

		DBObject options = BasicDBObjectBuilder.start().add("capped", false)
				.add("size", 4194304).get();
		db.createCollection("lotusCollection", options);

		DBObject firstObject = BasicDBObjectBuilder.start()
				.add("firstName", "saurav").append("lastName", "jain").get();
		db.getCollection("lotusCollection").insert(firstObject);
		
		BasicDBObject searchObject = new BasicDBObject();
		searchObject.append("firstName", "saurav");		
		DBCursor cursor = db.getCollection("").find(searchObject);
	}
}
