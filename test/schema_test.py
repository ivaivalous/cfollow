import json
from jsonschema import Draft3Validator, validate

client_schema_file=open("/var/lib/jenkins/workspace/cfollow-schema/trunk/communication-schema/client-message-schema.json")
server_schema_file=open("/var/lib/jenkins/workspace/cfollow-schema/trunk/communication-schema/server-message-schema.json")
client_example_file=open("/var/lib/jenkins/workspace/cfollow-schema/trunk/communication-schema/client-message-example.json")
server_example_file=open("/var/lib/jenkins/workspace/cfollow-schema/trunk/communication-schema/server-message-example.json")

client_schema = json.load(client_schema_file) 
server_schema = json.load(server_schema_file) 
client_example = json.load(client_example_file)
server_example = json.load(server_example_file)

Draft3Validator.check_schema(client_schema)
Draft3Validator.check_schema(server_schema)
validate(client_example, client_schema)
validate(server_example, server_schema)


