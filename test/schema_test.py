import json
import logging
from jsonschema import Draft3Validator, validate

logging.basicConfig(filename='schema_test_log.log',level=logging.DEBUG)
WORKSPACE_DIR = "/var/lib/jenkins/workspace/cfollow-schema/trunk/"
VERSION = "v1"

# Loading Schema files
client_schema_file = open(WORKSPACE_DIR + "schema/communication/client-message-schema." + VERSION + ".json")
server_schema_file = open(WORKSPACE_DIR + "schema/communication/server-message-schema." + VERSION + ".json")
client_configuration_schema_file = open(WORKSPACE_DIR + "schema/configuration/client-configuration-schema." + VERSION + ".json")
client_logging_schema_file = open(WORKSPACE_DIR + "schema/logging/client-logging-schema." + VERSION + ".json")

# Loading example files
client_example_file = open(WORKSPACE_DIR + "schema/communication/examples/client-message-example.json")
server_example_file = open(WORKSPACE_DIR + "schema/communication/examples/server-message-example.json")
client_configuration_example_file = open(WORKSPACE_DIR + "schema/configuration/examples/client-configuration-example.json")
client_logging_example_file = open(WORKSPACE_DIR + "schema/logging/examples/client-logging-example.json")

# Loading into JSON
client_schema = json.load(client_schema_file) 
server_schema = json.load(server_schema_file) 
client_configuration_schema = json.load(client_configuration_schema_file)
client_logging_schema = json.load(client_logging_schema_file)

client_example = json.load(client_example_file)
server_example = json.load(server_example_file)
client_configuration_example = json.load(client_configuration_example_file)
client_logging_example = json.load(client_logging_example_file)

# Running verification
logging.info("Testing schemes for compliance against the JSON Schema format")
Draft3Validator.check_schema(client_schema)
Draft3Validator.check_schema(server_schema)
Draft3Validator.check_schema(client_configuration_schema)
Draft3Validator.check_schema(client_logging_schema)

logging.info("Testing example files for compliance against respective schemes")
validate(client_example, client_schema)
validate(server_example, server_schema)
validate(client_configuration_example, client_configuration_schema)
validate(client_logging_example, client_logging_schema)

