import pymongo

# connect to mongoDB server
myclient = pymongo.MongoClient("mongodb://localhost:27017")

# create a database
mydb = myclient["mydatabase"]

# create a collection
mycol = mydb["customers"]

# use a python list to add many documents
myrecords = [{ "name": "John", "role": "customer" },
    { "name": "Jane", "role": "customer" },
    { "name": "Joe", "role": "employee" },
    { "name": "Julie", "role": "employee" }]

# insert into collection and get a result object, x
x = mycol.insert_many(myrecords)

# use a python dict to specify query parameters
myquery = {'role':'customer'}

# get and print all documents
for d in mycol.find(myquery):

  # use the dict method 'get' to get a value for a field
  print(d.get('name'))

# # use a python dict to specify query parameters
myquery = {'role':'customer'}
myquery_new = {'role' : 'consumer'}
mycursor = mycol.find()
for doc in mycursor:
  print(doc)

print()
mycol.update_many(myquery, {'$set': myquery_new})
mycursor = mycol.find()
for doc in mycursor:
  print(doc)

# find records using new parameter value
for d in mycol.find({'role' : 'consumer'}):
  print(d.get('name'))

# delete all documents with role==consumer
myquery_new = {'role' : 'consumer'}
mycol.delete_many(myquery_new)

mycursor = mycol.find()
for doc in mycursor:
  print(doc)

mycol.drop()
