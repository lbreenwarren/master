import pymongo

def create_username():
    mycol.create_index("username", unique=True)

    try:
        x = mycol.insert_one(user_data)
        return x.inserted_id
    except pymongo.errors.DuplicateKeyError:
        return None
    
# connect to mongoDB server
myclient = pymongo.MongoClient("mongodb://localhost:27017")

# create a database
mydb = myclient["mydatabase"]

# create a collection
mycol = mydb["users"]
mycursor = mycol.find()
for doc in mycursor:
  print(doc)

user_data = {"username": "foo"}
print(create_username())
mycursor = mycol.find()
for doc in mycursor:
  print(doc)

user_data = {"username": "bar"}
print(create_username())
mycursor = mycol.find()
for doc in mycursor:
  print(doc)

user_data = {"username": "bar"}
print(create_username())
mycursor = mycol.find()
for doc in mycursor:
  print(doc)
mycol.drop()