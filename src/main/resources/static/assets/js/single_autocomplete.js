function initAutocomplete() {
    var address = 'address';
    var autocomplete;
    autocomplete = new google.maps.places.Autocomplete((document.getElementById(address)), {
        types: ['geocode'],
    });
}


window.onload = function() {
    initAutocomplete();
};