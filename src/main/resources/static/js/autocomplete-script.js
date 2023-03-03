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
// // Set the coordinates of the selected location for place_from
//     google.maps.event.addListener(autocompleteFrom, 'place_changed', function() {
//         var place = autocompleteFrom.getPlace();
//         document.getElementById('coordinates_from').value = place.geometry.location;
//     });
// // Set the coordinates of the selected location for place_to
//     google.maps.event.addListener(autocompleteTo, 'place_changed', function() {
//         var place = autocompleteTo.getPlace();
//         document.getElementById('coordinates_to').value = place.geometry.location;
//     });
}


window.onload = function() {
    initAutocomplete();
};