function initAutocomplete() {

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


window.onload = function () {
    initAutocomplete();
};