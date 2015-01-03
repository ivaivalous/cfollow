# Cfollow Client Program
import json
import jsonschema
import sys
import logging

CONFIG_FILENAME               = "config.json"
LOG_FILENAME                  = "logs/client.log"
api_key                       = None
server_address                = None
crontab                       = None
default_connections_count     = 1
default_positions_count       = 1
disable_gps                   = True
enable_send_log               = False
enable_archive_log_after_send = True
config_schema                 = None

logging.basicConfig(filename=LOG_FILENAME, format="%(levelname)s|%(asctime)s|%(message)s", level=logging.DEBUG)

def read_config():
    global config_schema
    global api_key
    global server_address
    global crontab
    global default_connections_count
    global default_positions_count
    global disable_gps
    global enable_send_log
    global enable_archive_log_after_send

    config_file   = open(CONFIG_FILENAME)
    config        = json.load(config_file)
    config_schema = json.load(open("schema/client-configuration-schema.v1.json"))

    try:
        jsonschema.validate(config, config_schema)
    except jsonschema.ValidationError:
        logging.critical("Invalid configuration file. Cannot continue.")
        sys.exit(1)

    api_key                   = config["apiKey"]
    server_address            = config["serverAddress"]
    crontab                   = config["crontab"]
    default_connections_count = config["defaultConnectionsCount"]
    default_positions_count   = config["defaultPositionsCount"]
    disable_gps               = config["disableGps"]
    enable_send_log           = config["enableSendLog"]
    enable_archive_log_after_send = config["enableArchiveLogAfterSend"]

    logging.info("Loaded config. API key=" + api_key)

def build_logging_request_json():
    logging_json = None
    logging_data = []

    with open(LOG_FILENAME, "r") as log_file:
        log_data = log_file.read().split('\n')

    for record in log_data:
        if(record.isspace() or record == ''):
        	continue

    	record_data = record.split('|')
        assoc_data = {}

        try:
            assoc_data["date"]    = record_data[1]
            assoc_data["level"]   = record_data[0]
            assoc_data["message"] = record_data[2]
        except IndexError:
        	logging.error("Encountered malformed log entry. Skipped.")
        	continue

        logging_data.append(assoc_data)

    logging_json = json.dumps(logging_data)

    return logging_json


if __name__ == "__main__":
    read_config()