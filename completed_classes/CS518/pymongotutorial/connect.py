# pymongo allows python to speak with mongoDB
import pymongo

# connect to mongoDB server
myclient = pymongo.MongoClient("mongodb://localhost:27017")

# create a database
mydb = myclient["mydatabase"]

# list databases
print(myclient.list_database_names())

# create a collection
mycol = mydb["customers"]

# list connections
print(mydb.list_collection_names())

# use a python dict to specify data fields and values
mydict = { "name": "John", "address": "Highway 37" }

# insert into collection and get a result object, x
x = mycol.insert_one(mydict)

# print the id of the inserted document
print(x.inserted_id)

# get and print all documents - returns a cursor object
mycursor = mycol.find()

## you can convert a cursor to a list and print
#mylist = list(mycursor)
#print(mylist)

# or, you can iterate over a cursor or list
for doc in mycursor:
  print(doc)





