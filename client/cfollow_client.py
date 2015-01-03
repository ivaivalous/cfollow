# Cfollow Client Program
import json
import jsonschema
import sys
import logging
import shutil
import time
import os

CONFIG_FILENAME               = "config.json"
LOG_FILENAME                  = "logs/client.log"
LOG_ARCHIVE_DIRECTORY         = "logs/archive/"
LOG_ARCHIVE_FILENAME          = "_client.log"
SEPARATOR                     = "|"
SEPARATOR_REPLACEMENT         = "__PIPE__"
NEWLINE                       = "\n"
NEWLINE_REPLACEMENT           = "__NL__"

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

def sanitize_log_message(message):
    message_safe = message
    message_safe = message_safe.replace(SEPARATOR, SEPARATOR_REPLACEMENT)
    message_safe = message_safe.replace(NEWLINE, NEWLINE_REPLACEMENT)
    return message_safe

def get_log_size():
    return os.path.getsize(LOG_FILENAME)

def archive_log():
    shutil.copyfile(LOG_FILENAME, LOG_ARCHIVE_DIRECTORY + time.strftime("%H_%M_%S") + LOG_ARCHIVE_FILENAME)
    open(LOG_FILENAME, 'w').close()

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

    logging.info(sanitize_log_message("Loaded config. API key=" + api_key))

def build_logging_request_json():
    logging_json = None
    logging_data = {}
    logging_data["apiKey"] = api_key

    with open(LOG_FILENAME, "r") as log_file:
        log_data = log_file.read().split(NEWLINE)

    for record in log_data:
        if(record.isspace() or record == ''):
            continue

        record_data = record.split(SEPARATOR)
        assoc_data = {}

        if(len(record_data) != 3):
            logging.error(sanitize_log_message("Encountered malformed log entry \"" + record + "\" Skipped."))
            continue            

        try:
            assoc_data["date"]    = record_data[1]
            assoc_data["level"]   = record_data[0]
            assoc_data["message"] = record_data[2]
        except IndexError:
            logging.error(sanitize_log_message("Encountered malformed log entry \"" + record + "\" Skipped."))
            continue

        logging_data["log"] = assoc_data

    logging_json = json.dumps(logging_data)

    return logging_json

def validate_logging_request_json(logging_json):
    logging_schema = json.load(open("schema/client-logging-schema.v1.json"))
    try:
        jsonschema.validate(logging_json, logging_schema)
    except jsonschema.ValidationError:
        logging.error("Invalid log file. Will archive")
        archive_log()


if __name__ == "__main__":
    read_config()
    validate_logging_request_json(build_logging_request_json())