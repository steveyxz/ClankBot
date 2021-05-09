package com.clankBot.data;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("all")
public class DataManagerMongoDB {

    private MongoClient mainClient;
    private MongoDatabase mainDatabase;

    public DataManagerMongoDB(String dataBaseName) {
        String pword = "";
        try {
            pword = new String(this.getClass().getResourceAsStream("/mongoDBpword.txt").readAllBytes() == null ? new byte[] {} :this.getClass().getResourceAsStream("/mongoDBpword.txt").readAllBytes());
            if (System.getenv("MONGODB") != null) {
                pword = System.getenv("MONGODB");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainClient = MongoClients.create(
                "mongodb+srv://stvnzoo:" + pword + "@clankbot.fs803.mongodb.net/clankbot?retryWrites=true&w=majority");
        mainDatabase = mainClient.getDatabase(dataBaseName);

        System.out.println("Connected to " + dataBaseName);
    }

    public MongoCollection<Document> createCollection(String collectionName) {
        mainDatabase.createCollection(collectionName);
        return mainDatabase.getCollection(collectionName);
    }

    public boolean doesDocumentExist(String documentName, String collectionName) {
        //Return false if collection does not exist.
        MongoCollection<Document> collection;
        try {
            collection = mainDatabase.getCollection(collectionName);
        } catch (IllegalArgumentException e) {
            return false;
        }
        //Return true if document exists.
        FindIterable<Document> iterDoc = collection.find();
        for (Document document : iterDoc) {
            if (document.getString("ID").equals(documentName)) {
                return true;
            }
        }
        return false;

    }

    /**
     * This method creates a document with the attribute ID, which will contain the documentName.
     *
     * @param documentName   The name of the document being created.
     * @param collectionName The name of the collection being searched.
     * @return The created document or the specified document if the document already exists.
     */
    public Document createDocument(String documentName, String collectionName) {
        //Create collection if non-existent
        MongoCollection<Document> collection;
        try {
            collection = mainDatabase.getCollection(collectionName);
        } catch (IllegalArgumentException e) {
            collection = createCollection(collectionName);
        }
        //Return the document if document already exists
        FindIterable<Document> iterDoc = collection.find();
        for (Document document : iterDoc) {
            if (document.getString("ID").equals(documentName)) {
                return document;
            }
        }
        //Add document with ID
        Document addDocument = new Document("ID", documentName);
        //Insert document to collection
        collection.insertOne(addDocument);
        //Return document
        iterDoc = collection.find();
        for (Document document : iterDoc) {
            if (document.getString("ID").equals(documentName)) {
                return document;
            }
        }
        return null;
    }

    /**
     * Gets the value of a key inside a document inside a collection.
     *
     * @param collectionName THe name of the collection.
     * @param documentName   The name of the document
     * @param key            The key that you are searching for.
     * @return The value of the key, or null if the collection, document or key does not exist.
     */
    public Object getValueOfKey(String collectionName, String documentName, String key) {
        //Return null if collection does not exist.
        MongoCollection<Document> collection;
        try {
            collection = mainDatabase.getCollection(collectionName);
        } catch (IllegalArgumentException e) {
            return null;
        }
        //Return object if document exists.
        FindIterable<Document> iterDoc = collection.find();
        for (Document document : iterDoc) {
            if (document.getString("ID").equals(documentName)) {
                return document.get(key);
            }
        }
        //Object not found.
        return null;
    }

    /**
     * This method will create a document with the properties specified inside the collection specified.
     * This method is an extension of its counterpart (@<code>createDocument(documentName, collectionName)</code>)
     * in the fact that you can specify starting values for the document.
     *
     * @param documentName   The name of the document being created.
     * @param collectionName The name of the collection that the document will be created in.
     * @param values         The values you want to add to the document during creation.
     * @return The document that was created or the document you specified if it already exists.
     */
    public Document createDocument(String documentName, String collectionName, HashMap<String, Object> values) {
        //Create collection if non-existent
        MongoCollection<Document> collection;
        try {
            collection = mainDatabase.getCollection(collectionName);
        } catch (IllegalArgumentException e) {
            collection = createCollection(collectionName);
        }
        //Return the document if document already exists
        FindIterable<Document> iterDoc = collection.find();
        for (Document document : iterDoc) {
            if (document.getString("ID").equals(documentName)) {
                return document;
            }
        }
        //Initialize the document
        Document addDocument = new Document("ID", documentName);
        for (int i = 0; i < values.keySet().size(); i++) {
            addDocument.append((String) values.keySet().toArray()[i], values.get(values.keySet().toArray()[i]));
        }
        //Insert the document
        collection.insertOne(addDocument);
        //Return the document
        iterDoc = collection.find();
        for (Document document : iterDoc) {
            if (document.getString("ID").equals(documentName)) {
                return document;
            }
        }
        //At here it means the document was not created successfully.
        return null;
    }

    /**
     * This method inserts a set of values into a document, overriding all of the existent values in that document,
     * This method will automatically create a document or collection if the specified one is non existent.
     *
     * @param collectionName The name of the collection used. Will create a new collection if collection specified
     *                       is non-existent.
     * @param documentName   The name of the document that you are inserting to. If the document does not exist,
     *                       this method will create a new one.
     * @param values         The values that are added to the document. Formatted in a HashMap.
     */
    public void insertToDocument(String collectionName, String documentName, HashMap<String, Object> values) {
        //Creating the collection if non-existent.
        MongoCollection<Document> collection;
        try {
            collection = mainDatabase.getCollection(collectionName);
        } catch (IllegalArgumentException e) {
            collection = createCollection(collectionName);
        }
        //Check if document exists
        FindIterable<Document> iterDoc = collection.find();
        boolean doesDocumentExist = false;
        for (Document document : iterDoc) {
            if (document.getString("ID").equals(documentName)) {
                doesDocumentExist = true;
                break;
            }
        }
        //Create document if non existent.
        if (!doesDocumentExist) {
            createDocument(documentName, collectionName, values);
        }

        //Insert to the document.
        for (int i = 0; i < values.keySet().size(); i++) {
            collection.updateOne(Filters.eq("ID", documentName),
                    Updates.set((String) values.keySet().toArray()[i],
                            values.get(values.keySet().toArray()[i])));
        }
    }

    /**
     * This method gets a document with a specific name in a specfic collection.
     *
     * @param documentName   The name of the document that is to be found.
     * @param collectionName The name of the collection that is being searched for.
     * @return The document if the document exists, or else just null.
     */
    public Document getDocument(String documentName, String collectionName) {
        //Create collection if non-existent
        MongoCollection<Document> collection;
        try {
            collection = mainDatabase.getCollection(collectionName);
        } catch (IllegalArgumentException e) {
            collection = createCollection(collectionName);
        }
        //Return the document if document exists.
        FindIterable<Document> iterDoc = collection.find();
        for (Document document : iterDoc) {
            if (document.getString("ID").equals(documentName)) {
                return document;
            }
        }
        //Non-existent document: return null.
        return null;
    }

}
