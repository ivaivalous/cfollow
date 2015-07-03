// Actual script starting point
// Make sure the browser supports local storage
if(typeof(Storage) === "undefined") {
  console.log("This page uses local storage which your browser doesn't support.");
}

window.hideUserSections = function() {
  $("#welcome-section").hide();
  $("#map-section").hide();
  $("#con-section").hide();
  $("#log-section").hide();
};

// UI
// If a user has been loaded, don't show the login form
if(localStorage.user !== undefined) {
  var json = JSON.parse(localStorage.user);
  
  $("#login-box").hide();
  $("#user-box").show();

  $("#user-box-name").text(json.nicename);
  $("#user-box-email").html('<a href="mailto:' + json.email + '">' + json.email + '</a>');
} else {
  $("#login-box").show();
  $("#user-box").hide();
  hideUserSections();
};

// Behavior 
// Login form submit
$('#login-submit').click(function() {
  var username = $('#user').val();
  var password = $('#pass').val();
  
  // Don't make a request if no user or pass have been given
  if(username == "" || password == "") {
    return;
  }

  $.ajax({
  url: "https://ivo.qa/follow/webadmin/userLogin", 
  type: "POST",
  data: {"username": username, "password" : password},
  success: function(data, status, arguments) { 
      var json = data; 

      if(json.success === false) {
        $('#login-notification').text('We couldn\'t recognise these credentials. Try again?');
      } else {
        localStorage.setItem("user", JSON.stringify(data));
        location.reload();
      }
    }
  });

});


$('#log-out').click(function() {
  localStorage.clear();
  location.reload();
});

window.showLogScreen = function() {
  buildLogScreen();

  $("#welcome-section").hide();
  $("#map-section").hide();
  $("#con-section").hide();
  $("#log-section").show();
};

window.formatDate = function(date) {
  var seconds = date.getSeconds();
  var minutes = date.getMinutes();
  var hours = date.getHours();

  if(seconds < 10) {
    seconds = '0' + seconds;
  }
  if(minutes < 10) {
    minutes = '0' + minutes;
  }
  if(hours < 10) {
    hours = '0' + hours;
  }

  return hours + ':' + minutes + ':' + seconds + ' ' + date.getDate() + '.' + (Number(date.getMonth()) + 1) + '.' + date.getFullYear();
};

window.buildLogScreen = function() {
  var json = JSON.parse(localStorage.log);
  var info = "<img class='title-image' src='https://s3-us-west-2.amazonaws.com/s.cdpn.io/235522/env.png' alt='logs' /><p>This screen provides logs the Follow device sent to the server. You can use logs to better understand what is happening to your device.</p>";
  var table = '<table id="logs-table"><tr><th>Time</th><th>Log Level</th><th>Message</th></tr>';

  for (var i = 0; i < json.log.length; i++) {
      var entry = json.log[i];
      var level = entry.level.toLowerCase();

      table += '<tr class="' + level + '"><td>' + formatDate(new Date(entry.date)) + '</td><td>' + level + '</td><td>' + entry.message + '</td></tr>';
  }

  $("#log-section").html(info + table);
};

window.buildConnectionsScreen = function() {
  var json = JSON.parse(localStorage.con);
  var table = '<table id="connections-table"><tr><th>Time</th><th>Connection IP Address</th><th>Network SSID</th></tr>';

  for (var i = 0; i < json.connections.length; i++) {
      var entry = json.connections[i];

      table += '<tr><td>' + formatDate(new Date(entry.date)) + '</td><td><a href="https://who.is/whois-ip/ip-address/' + entry.ip + '" target="_new">' + entry.ip + '</a>' + '</td><td>' + entry.ssid + '</td></tr>';
  }

  $("#con-section").html(table);
};

window.getLatestCoordinates = function() {
  var json = JSON.parse(localStorage.map);
  return json.positions[json.positions.length - 1];
}

function initializeGoogleMaps() {
  var json = JSON.parse(localStorage.map);
  var initialPosition = getLatestCoordinates();

  map = new google.maps.Map(document.getElementById('map-section'), {
    zoom: 8,
    center: {lat: initialPosition.latitude, lng: initialPosition.longitude}
  });

  var bounds = new google.maps.LatLngBounds();
  for(var i = 0; i < json.positions.length; i++) { 
        var position = json.positions[i];
        var latLng = new google.maps.LatLng(position.latitude, position.longitude);
        bounds.extend(latLng);

        marker = new google.maps.Marker({
            position: latLng,
            map: map,
            title: '' + new Date(position.date)
        });
  }
  map.fitBounds(bounds);
}

window.buildMapScreen = function() {
  var map;
  initializeGoogleMaps();
  google.maps.event.addDomListener(window, 'load', initializeGoogleMaps);
};

window.showConnectionScreen = function() {
  buildConnectionsScreen();

  $("#welcome-section").hide();
  $("#map-section").hide();
  $("#con-section").show();
  $("#log-section").hide();
};

window.showMapScreen = function() {
  buildMapScreen();

  $("#welcome-section").hide();
  $("#map-section").show();
  $("#con-section").hide();
  $("#log-section").hide();
};

// Get and show logs
$('#log').click(function() {
  $.ajax({
    url: "https://ivo.qa/follow/webadmin/getClientLog", 
    headers: {'Authorization': JSON.parse(localStorage.user).jwt},
    success: function(data, status) { 
        var json = data; 

        if(json.success === false) {
          $('#log-notification').text("Didn\'t find any logs. Maybe you should log in again.");
        } else {
          localStorage.setItem("log", JSON.stringify(data));
          showLogScreen();
        }
      }
    })
});

// Get and show connections list
$('#con').click(function() {
  $.ajax({
    url: "https://ivo.qa/follow/webadmin/getClientConnections", 
    headers: {'Authorization': JSON.parse(localStorage.user).jwt},
    success: function(data, status) { 
        var json = data; 

        if(json.success === false) {
          $('#con-notification').text("Didn\'t find any connections. Maybe you should log in again.");
        } else {
          localStorage.setItem("con", JSON.stringify(data));
          showConnectionScreen();
        }
      }
    })
});

// Get and show the maps list
$('#map').click(function() {
  $.ajax({
    url: "https://ivo.qa/follow/webadmin/getClientPositions", 
    headers: {'Authorization': JSON.parse(localStorage.user).jwt},
    success: function(data, status) { 
        var json = data; 

        if(json.success === false) {
          $('#map-notification').text("Didn\'t find any positions. Maybe you should log in again.");
        } else {
          localStorage.setItem("map", JSON.stringify(data));
        }
      }
    });

  showMapScreen();
});