function initAutocomplete() {
    var placeFromInput = 'origin';
    var placeToInput = 'destination';
    var autocompleteFrom, autocompleteTo;
    autocompleteFrom = new google.maps.places.Autocomplete((document.getElementById(placeFromInput)), {
        types: ['geocode'],
    });
    autocompleteTo = new google.maps.places.Autocomplete((document.getElementById(placeToInput)), {
        types: ['geocode'],
    });

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
        }
    });
}


window.onload = function () {
    initAutocomplete();
};