# Cfollow Client Program
import json
import jsonschema
import sys

CONFIG_FILENAME = "config.json"
apiKey          = None
serverAddress   = None
crontab         = None
defaultConnectionsCount = 1
defaultPositionsCount   = 1
disableGps      = True
config_schema   = None

def read_config():
	global config_schema
	global apiKey
	global serverAddress
	global crontab
	global defaultConnectionsCount
	global defaultPositionsCount
	global disableGps

	config_file   = open(CONFIG_FILENAME)
	config        = json.load(config_file)
	config_schema = json.load(open("schema/client-configuration-schema.v1.json"))

	try:
		validate(config, config_schema)
	except ValidationError:
		sys.exit(1)

	apiKey                  = config["apiKey"]
	serverAddress           = config["serverAddress"]
	crontab                 = config["crontab"]
	defaultConnectionsCount = config["defaultConnectionsCount"]
	defaultPositionsCount   = config["defaultPositionsCount"]
	disableGps              = config["disableGps"]

def read_gps():
	pass

def build_request():
	pass

def connect_to_wifi():
	pass

def read_response():
	pass

def update_config():
	pass