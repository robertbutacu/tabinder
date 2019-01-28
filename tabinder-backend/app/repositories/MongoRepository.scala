package repositories

import models.Tab
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry

abstract class MongoRepository[T](databaseName: String, collectionName: String) {
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Tab]), DEFAULT_CODEC_REGISTRY )

  // To directly connect to the default server localhost on port 27017
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Tab] = database.getCollection(collectionName)
}
