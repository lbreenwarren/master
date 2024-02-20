# pymongo allows python to speak with mongoDB
import pymongo

# connect to mongoDB server
myclient = pymongo.MongoClient("mongodb://localhost:27017/")

# create a database
mydb = myclient["mydatabase"]
# connect to collection (or create if not exists)
mycol = mydb["customers"]

# drop the collection - delete all documents
mycol.drop()

# get and print all documents 
for d in mycol.find():
  print(d)