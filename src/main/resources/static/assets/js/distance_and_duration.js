window.onload = function() {

// Get the origin and destination addresses from the input fields
    var origin = document.getElementById("origin").value;
    var destination = document.getElementById("destination").value;

    // Create a Directions Service object
    var service = new google.maps.DirectionsService();

    // Define the request parameters
    var request = {
        origin: origin,
        destination: destination,
        travelMode: google.maps.TravelMode.DRIVING,
        unitSystem: google.maps.UnitSystem.METRIC
    };

    // Send the request to the Directions API
    service.route(request, function (response, status) {
        if (status == google.maps.DirectionsStatus.OK) {
            // Extract the distance and duration values from the response
            var distance_text = response.routes[0].legs[0].distance.text;
            var distance_value = response.routes[0].legs[0].distance.value / 1000;  // Convert meters to kilometers
            var duration_text = response.routes[0].legs[0].duration.text;
            var duration_value = response.routes[0].legs[0].duration.value / 3600;  // Convert seconds to hours

            // Display the distance and duration values in the output elements
            var distance_field = document.getElementById("distance");
            distance_field.value = distance_text;
            var duration_field = document.getElementById("duration");
            duration_field.value = duration_text;

            // Display the route on the map
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 10,
                center: response.routes[0].legs[0].start_location
            });
            var directionsRenderer = new google.maps.DirectionsRenderer({
                map: map,
                directions: response,
                draggable: true,
                polylineOptions: {
                    strokeColor: 'green',
                    strokeWeight: 5,
                    strokeOpacity: 0.7
                }
            });
        } else {
            // Display an error message if the request fails
            var distance_field = document.getElementById("distance");
            distance_field.value = "Error: " + status;
            var duration_field = document.getElementById("duration");
            duration_field.value = "";
        }
    });
}
/*
function calculateDistance() {
    // Get the origin and destination addresses from the input fields
    var origin = document.getElementById("origin").value;
    var destination = document.getElementById("destination").value;

    // Create a Directions Service object
    var service = new google.maps.DirectionsService();

    // Define the request parameters
    var request = {
        origin: origin,
        destination: destination,
        travelMode: google.maps.TravelMode.DRIVING,
        unitSystem: google.maps.UnitSystem.METRIC
    };

    // Send the request to the Directions API
    service.route(request, function(response, status) {
        if (status == google.maps.DirectionsStatus.OK) {
            // Extract the distance and duration values from the response
            var distance_text = response.routes[0].legs[0].distance.text;
            var distance_value = response.routes[0].legs[0].distance.value / 1000;  // Convert meters to kilometers
            var duration_text = response.routes[0].legs[0].duration.text;
            var duration_value = response.routes[0].legs[0].duration.value / 3600;  // Convert seconds to hours

            // Display the distance and duration values in the output elements
            var distance_field = document.getElementById("distance");
            distance_field.value = distance_text;
            var duration_field = document.getElementById("duration");
            duration_field.value = duration_text;

            // Display the route on the map
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 10,
                center: response.routes[0].legs[0].start_location
            });
            var directionsRenderer = new google.maps.DirectionsRenderer({
                map: map,
                directions: response,
                draggable: true,
                polylineOptions: {
                    strokeColor: 'green',
                    strokeWeight: 5,
                    strokeOpacity: 0.7
                }
            });
        } else {
            // Display an error message if the request fails
            var distance_field = document.getElementById("distance");
            distance_field.value = "Error: " + status;
            var duration_field = document.getElementById("duration");
            duration_field.value = "";
        }
    });
}*/


