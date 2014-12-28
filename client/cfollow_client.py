# Cfollow Client Program
import json

CONFIG_FILENAME = "config.json"
apiKey = None
serverAddress = None
crontab = None
defaultConnectionsCount = 1
defaultPositionsCount = 1
disableGps = True

def read_config():
	config_file = open(CONFIG_FILENAME)
	config = json.load(config_file)
