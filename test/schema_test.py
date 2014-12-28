import json
from jsonschema import Draft3Validator, validate

client_schema_file=open("/var/lib/jenkins/workspace/cfollow-schema/trunk/communication-schema/client-message-schema.v1.json")
server_schema_file=open("/var/lib/jenkins/workspace/cfollow-schema/trunk/communication-schema/server-message-schema.v1.json")
client_example_file=open("/var/lib/jenkins/workspace/cfollow-schema/trunk/communication-schema/examples/client-message-example.json")
server_example_file=open("/var/lib/jenkins/workspace/cfollow-schema/trunk/communication-schema/examples/server-message-example.json")

client_schema = json.load(client_schema_file) 
server_schema = json.load(server_schema_file) 
client_example = json.load(client_example_file)
server_example = json.load(server_example_file)

print "Testing client and server schema."
Draft3Validator.check_schema(client_schema)
Draft3Validator.check_schema(server_schema)
print "Testing example files."
validate(client_example, client_schema)
validate(server_example, server_schema)
print "Schema test complete."


